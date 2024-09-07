package com.tong.service.user;

import com.tong.pojo.User;

import java.util.List;

public interface UserService {
    public User login(String userCode,String userPassword);
    //根据id修改密码
    public String updatePwdById(int id,String password);
    //根据角色或名字查询
    public int getUserCount(String username,int userRole);
    //根据条件查询用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

    //添加用户
    public boolean add(User user);
    //根据userCode查出user
    public User selectUserCodeExist(String userCode);

    //修改用户
    public boolean modify(User user);

    // 根据ID查找user
    public User getUserById(String id);

    // 根据id删除用户
    public boolean deleteUserById(int id);
}
