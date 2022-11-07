package com.dy.orm.helper;

import com.dy.orm.annotations.Id;
import com.dy.orm.annotations.Table;
import com.dy.orm.entity.Student;
import com.dy.orm.exception.ArgumentException;
import com.dy.orm.exception.FieldException;
import com.dy.orm.exception.TableException;
import com.dy.orm.mapper.BaseMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

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
        String tableName = getTableName(clazz);
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
        return BaseMapper.executeUpdate(sql, params);
    }

    /**
     * 获取表名
     * @param clazz 需要获取的类的名字
     * @return 表名
     */
    private String getTableName(Class clazz) {

        boolean existTableAnnotation = clazz.isAnnotationPresent(Table.class);
        // 注解是否存在
        if (!existTableAnnotation) {
            throw TableException.annotationNOtFound(clazz);
        }
        Table table = (Table)clazz.getAnnotation(Table.class);
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
        System.out.println(sql.toString());
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

}
