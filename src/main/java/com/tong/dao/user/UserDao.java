package com.tong.dao.user;

import com.tong.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    // 得到要登录的用户
    public User getLoginUser(Connection connection, String userCode);
    //修改当前用户密码
    public String updatePwd(Connection connection,int id,String password) throws SQLException;
    //查询用户总数
    public int getUserCount(Connection connection,String username,int userRole) throws SQLException;
    //获取用户列表并进行分页
    public List<User> getUserList(Connection connection,String username,int userRole,int currentPageNo,int pageSize) throws SQLException;
    //增加用户
    public int add(Connection connection,User user) throws SQLException;
    //修改用户
    public int modify(Connection connection,User user) throws SQLException;
    // 通过userId获取user
    public User getUserById(Connection connection, String id)throws Exception;
    // 删除用户
    public int deleteUser(Connection connection,int id) throws SQLException;
}

