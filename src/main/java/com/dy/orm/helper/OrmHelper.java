package com.dy.orm.helper;

import com.dy.orm.annotations.Column;
import com.dy.orm.annotations.Id;
import com.dy.orm.annotations.Table;
import com.dy.orm.exception.ArgumentException;
import com.dy.orm.exception.FieldException;
import com.dy.orm.exception.TableException;
import com.dy.orm.mapper.BaseMapper;
import com.dy.orm.utils.GetterSetterUtils;
import com.dy.orm.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ORM 核心实现类 实现BaseMapper操作数据库
 *
 * 1. 得到各种操作的SQL语句
 * 2. 得到SQL语句的参数
 *
 * @author zhanglianyong
 * 2022/11/7 21:36
 */
public class OrmHelper<E> {

    /**
     * 将对象的参数转成sql并添加到数据库
     * insert into table_name(字段名) values(?,?,?);
     * @param element 对象
     * @return 插入是否成功
     */
    public int insert(E element) {

        if (null == element) {
            throw ArgumentException.IllegalArgumentException("插入对象不能为空！");
        }

        Class<?> clazz = element.getClass();
        String tableName = getTableName((Class<E>) clazz);
        // 获取字段
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            throw new FieldException(element + "没有属性");
        }

        // 并且插入的SQL
        String sql = getInsertSql(tableName, fields);
        // 获取参数
        Object[] params = getSqlParams(element, fields);
        // 执行插入或者更新
        return new BaseMapper<E>().executeUpdate(sql, params);
    }

    /**
     * 根据对象的主键查询对象数据
     *
     * select , , , , from table_name where id = ?
     *
     * @param element 对象
     * @return 查询到的数据
     */
    public List<E> selectByPrimaryKey(E element) {

        if (null == element) {
            throw ArgumentException.IllegalArgumentException("查询的对象不能为空");
        }
        Class<?> clazz = element.getClass();
        PropertyFieldMapping<String, Id> propertyFieldMapping = getIdName((Class<E>) clazz);
        // 查询id
        String idPropertyName = propertyFieldMapping.getProperty();
        String idColumnName = propertyFieldMapping.getAnnotation().value();
        String getMethodName = GetterSetterUtils.createGet(idPropertyName);
        Object id = doMethod((Class<E>) clazz, getMethodName, element, null);
        if (null == id) {
            throw ArgumentException.IllegalArgumentException("主键id不能为空");
        }
        List<ColumnToPropertyMapping<Object, Object>> propertyMapping = getPropertyFieldMapping((Class<E>) clazz);
        propertyMapping.add(new ColumnToPropertyMapping<>(idPropertyName, idColumnName));
        StringBuilder sql = new StringBuilder().append("select ");
        // 拼接需要查询的列
        List<String> columns = getColumns((Class<E>) clazz);
        Object[] params = {id};
        for (String column : columns) {
            sql.append(column + ",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" from ").append(getTableName((Class<E>) clazz)).append(" where ").append(idColumnName);
        sql.append(" = ?" + ";");
        return new BaseMapper<E>().executeSelect(sql.toString(), element, propertyMapping, params);
    }

    /**
     * 获取表名
     * @param clazz 需要获取的类的名字
     * @return 表名
     */
    private String getTableName(Class<E> clazz) {

        boolean existTableAnnotation = clazz.isAnnotationPresent(Table.class);
        // 注解是否存在
        if (!existTableAnnotation) {
            throw TableException.annotationNOtFound(clazz);
        }
        Table table = clazz.getAnnotation(Table.class);
        return table.value();
    }

    /**
     * 拼接sql
     * insert into table_name() values(?, ?, ?)
     * @param tableName 表名
     * @param fields 对象包含的属性
     * @return 拼接的sql
     */
    private String getInsertSql(String tableName, Field[] fields) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tableName).append(" values(");

        for (int i = 0; i < fields.length; i++) {
            sql.append("?,");
        }
        // values(?,?,  delete ,
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        return sql.toString();
    }

    /**
     * 根据对象SQL回去语句的参数
     *
     * @param element 对象
     * @param fields 对象包含的属性
     * @return SQL的参数
     */
    private Object[] getSqlParams(E element, Field[] fields) {

        Object[] params = new Object[fields.length];
        Id idAnnotation;
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            // 获取这个对象的值
            try {
                idAnnotation = fields[i].getAnnotation(Id.class);
                if (null != idAnnotation) {
                    if (idAnnotation.increment()) {
                        params[i] = null;
                    } else {
                        params[i] = fields[i].get(element);
                    }
                } else {
                    params[i] = fields[i].get(element);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    /**
     * 获取主键注解Id的值
     *
     * @param clazz 属性
     * @return Id注解的值
     */
    private PropertyFieldMapping<String, Id> getIdName(Class<E> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        PropertyFieldMapping<String, Id> propertyFieldMapping = getId(fields);
        if (null == propertyFieldMapping) {
            throw ArgumentException.IllegalArgumentException(clazz + "没有@Id注解");
        }
        if (null == propertyFieldMapping.getAnnotation()) {
            throw ArgumentException.IllegalArgumentException(clazz + "没有@Id注解");
        }
        return propertyFieldMapping;
    }

    /**
     * 获取指定对象的Id注解值
     *
     * @param fields 属性
     * @return Id对象注解的信息
     */
    private PropertyFieldMapping<String, Id> getId(Field[] fields) {
        PropertyFieldMapping<String, Id> propertyFieldMapping = new PropertyFieldMapping<>();
        for (Field declaredField : fields) {
            if (declaredField.isAnnotationPresent(Id.class)) {
                declaredField.setAccessible(true);
                Id id = declaredField.getAnnotation(Id.class);
                propertyFieldMapping.setProperty(declaredField.getName());
                propertyFieldMapping.setAnnotation(id);
                return propertyFieldMapping;
            }
        }
        return null;
    }

    /**
     * 获取所有的列
     *
     * @param clazz 对象
     * @return 返回所有的列
     */
    private List<String> getColumns(Class<E> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> columns = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            columns.add(field.getName());
        }
        return columns;
    }

    /**
     * 通过反射调用某个对象的某一个方法
     *
     * @param clazz 类
     * @param methodName 方法名
     * @param element 对象
     * @param params 传入的参数
     * @return 执行方法的值
     */
    private Object doMethod(Class<E> clazz, String methodName, E element,  Class<?>... params) {
        Method method;
        try {
            System.out.println(methodName);
            method = clazz.getMethod(methodName, params);
            method.setAccessible(true);
            return method.invoke(element, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回属性和数据库列的mapping映射
     *
     * @param clazz 类
     * @return 属性和数据库列的对应
     */
    private List<ColumnToPropertyMapping<Object, Object>> getPropertyFieldMapping(Class<E> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<ColumnToPropertyMapping<Object, Object>> mappings = new ArrayList<>();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            if (!declaredField.isAnnotationPresent(Column.class) && !declaredField.isAnnotationPresent(Id.class)) {
                throw ArgumentException.IllegalArgumentException(declaredField.getName() + "没有@Column注解");
            }
            Column column = declaredField.getAnnotation(Column.class);
            if (null != column) {
                StringUtils.getStringNotNull(column.value());
                mappings.add(new ColumnToPropertyMapping<>(declaredField.getName(), column.value()));
            }
        }
        return mappings;
    }
}
