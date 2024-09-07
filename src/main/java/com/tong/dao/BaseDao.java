package com.tong.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private final static String url;
    private final static String username;
    private final static String password;
    private final static String driver;

    static {
        Properties properties = new Properties();
        //通过类加载器读取对应资源
        InputStream resource = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    public static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    //编写查询公共类
    public static ResultSet execute(Connection connection,String sql,Object[] params,ResultSet rs,PreparedStatement statement) throws SQLException {
        statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i+1,params[i]);
        }
        rs = statement.executeQuery();
        return rs;
    }

    //增删改公共方法
    public static int update(Connection connection,String sql,Object[] params,PreparedStatement statement) throws SQLException {
        statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i+1,params[i]);
        }
        return statement.executeUpdate();
    }

    //释放资源
    public static boolean release(Connection connection,PreparedStatement statement,ResultSet resultSet){
        boolean flag = true;
        if (resultSet!=null){
            try {
                resultSet.close();
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
