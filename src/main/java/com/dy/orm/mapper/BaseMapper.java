package com.dy.orm.mapper;

import com.dy.orm.entity.Student;
import com.dy.orm.exception.ArgumentException;
import com.dy.orm.helper.ColumnToPropertyMapping;
import com.dy.orm.helper.PropertyFieldMapping;
import com.dy.orm.utils.CollectionUtils;
import com.dy.orm.utils.MySQLUtils;
import com.dy.orm.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 基础访问操作库的基类
 *
 * @author zhanglianyong
 * 2022/11/7 21:18
 */
public class BaseMapper<E> {

    public static final Logger logger = LoggerFactory.getLogger(BaseMapper .class);

    /**
     * 执行更新操作
     * @param sql 执行的sql
     * @param params 传入的参数
     * @return 返回是否执行成功
     */
    public int executeUpdate(String sql, Object... params) {
        // 执行失败
        int result = -1;
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = MySQLUtils.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            result = ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("更新数据库异常");
            logger.error(ex.getMessage());
        } finally {
            MySQLUtils.close(connection, ps);
        }

        return result;
    }

    public List<E> executeSelect(String sql, E element, List<ColumnToPropertyMapping<Object, Object>> mappings,
                                 Object... params) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet;
        List<E> ret = new ArrayList<>();
        if (CollectionUtils.CollectionIsNull(mappings)) {
            throw new ArgumentException("没有配置数据库字段注解和属性之间的映射，请检查实体是否有填写相应的注解", new RuntimeException());
        }
        Class<?> clazz = element.getClass();
        E returnType;
        Field field;
        try {
            connection = MySQLUtils.getConnection();
            ps = connection.prepareStatement(sql);
            if (!StringUtils.ObjectsIsNull(params)) {
                // 填入？
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                returnType = (E)clazz.newInstance();
                // 获取数据
                for (ColumnToPropertyMapping<Object, Object> mapping : mappings) {
                    Object property = mapping.getProperty();
                    field = clazz.getDeclaredField(String.valueOf(property));
                    field.setAccessible(true);
                    field.set(returnType, resultSet.getObject(String.valueOf(mapping.getColumn())));
                }
                ret.add(returnType);
            }
        } catch (Exception ex) {
            System.out.println("查询数据库失败");
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            MySQLUtils.close(connection, ps);
        }
        return ret;
    }

}
