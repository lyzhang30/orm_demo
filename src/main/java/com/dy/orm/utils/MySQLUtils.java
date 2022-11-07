package com.dy.orm.utils;

import com.dy.orm.exception.DataBaseException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLUtils {

    private Connection connection;

    private PreparedStatement pstm;

    public static Connection getConnection() {
        Properties properties = new Properties();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        try {
            properties.load(inputStream);
            String driver = properties.getProperty("driver");
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            e.printStackTrace();
            throw DataBaseException.failToConnect("连接数据库失败", e);
        }
    }

    public static void close(Connection connection, PreparedStatement pstm) {

        if (null != pstm) {
            try {
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public static void main(String[] args) {
        Connection connection = getConnection();
        System.out.println(connection);
    }

}
