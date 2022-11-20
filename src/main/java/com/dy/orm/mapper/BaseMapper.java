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
public class BaseMapper<E> implements Mapper<E> {

    public static final Logger logger = LoggerFactory.getLogger(BaseMapper.class);

    private Connection connection;

    private PreparedStatement ps;

    private boolean autoCommit;


    public BaseMapper() {
        this.connection = MySQLUtils.getConnection();
    }

    public BaseMapper(boolean autoCommit) {
        this.connection = MySQLUtils.getConnection();
        this.autoCommit = autoCommit;
    }

    /**
     * 执行更新操作
     * @param sql 执行的sql
     * @param params 传入的参数
     * @return 返回是否执行成功
     */
    public int executeUpdate(String sql, Object... params) {
        // 执行失败
        int result = -1;
        try {
            setAutoCommitFor();
            ps = connection.prepareStatement(sql);
            setParams(ps, params);
            result = ps.executeUpdate();
            commitConnection();
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

        List<E> ret = new ArrayList<>();
        if (CollectionUtils.CollectionIsNull(mappings)) {
            throw new ArgumentException("没有配置数据库字段注解和属性之间的映射，请检查实体是否有填写相应的注解", new RuntimeException());
        }
        Class<?> clazz = element.getClass();
        E returnType;
        Field field;
        try {
            setAutoCommitFor();
            ps = connection.prepareStatement(sql);
            setParams(ps, params);
            ResultSet resultSet = ps.executeQuery();
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
            commitConnection();
        } catch (Exception ex) {
            System.out.println("查询数据库失败");
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            MySQLUtils.close(connection, ps);
        }
        return ret;
    }


    private void setParams(PreparedStatement ps, Object... params) throws Exception{
        if (!StringUtils.ObjectsIsNull(params)) {
            // 填入？
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }
    }

    /**
     * 提交事务
     */
    private void commitConnection() {
        try {
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("不能提交" + e);
        }

    }

    /**
     * 设置自动提交
     */
    private void setAutoCommitFor() {
        try {
            if (this.autoCommit != connection.getAutoCommit()) {
                connection.setAutoCommit(autoCommit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("不能设置自动提交", e);
        }
    }

    public boolean getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit() {
        this.autoCommit = false;
    }
}
