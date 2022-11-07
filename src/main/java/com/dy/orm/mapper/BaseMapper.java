package com.dy.orm.mapper;

import com.dy.orm.utils.MySQLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 基础访问操作库的基类
 *
 * @author zhanglianyong
 * 2022/11/7 21:18
 */
public class BaseMapper {

    public static final Logger logger = LoggerFactory.getLogger(BaseMapper .class);

    /**
     * 执行更新操作
     * @param sql 执行的sql
     * @param params 传入的参数
     * @return 返回是否执行成功
     */
    public static int executeUpdate(String sql, Object... params) {
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


}
