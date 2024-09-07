package com.tong.service.user.impl;

import com.tong.dao.BaseDao;
import com.tong.dao.user.UserDao;
import com.tong.dao.user.impl.UserDaoImpl;
import com.tong.pojo.User;
import com.tong.service.user.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String userPassword) {
        Connection connection = null;
        User user = null;

        connection = BaseDao.getConnection();
        user = userDao.getLoginUser(connection, userCode);

        BaseDao.release(connection,null,null);
        return user;
    }

    @Override
    public String updatePwdById(int id, String password) {

        Connection connection = null;
        String flag = null;
        try {
            connection = BaseDao.getConnection();
            if (userDao.updatePwd(connection,id,password) !=null){
                flag = "修改成功";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.release(connection,null,null);
        }

        return flag;
    }

    @Override
    public int getUserCount(String username, int userRole) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, username, userRole);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.release(connection,null,null);
        }

        return count;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.release(connection,null,null);
        }

        return userList;
    }

    @Override
    public boolean add(User user) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            //开启事务
            connection.setAutoCommit(false);
            //调用dao层访问数据
            int addNum = userDao.add(connection, user);
            //提交事务
            connection.commit();
            if (addNum>0){
                flag = true;
                System.out.println("添加成功");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("=======出现异常，事务回滚===========");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally {
            BaseDao.release(connection,null,null);
        }
        return flag;
    }

    @Override
    public User selectUserCodeExist(String userCode) {
        Connection connection = null;
        User user = null;

        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } finally {
            BaseDao.release(connection,null,null);
        }
        return user;
    }

    @Override
    public boolean modify(User user) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (userDao.modify(connection,user)>0){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.release(connection,null,null);
        }
        return flag;
    }

    @Override
    public User getUserById(String id) {
        User user = null;
        Connection connection = null;
        try{
            connection = BaseDao.getConnection();
            user = userDao.getUserById(connection,id);
        }catch (Exception e) {
            e.printStackTrace();
            user = null;
        }finally{
            BaseDao.release(connection, null, null);
        }
        return user;
    }

    //根据id删除用户

    @Override
    public boolean deleteUserById(int id) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (userDao.deleteUser(connection,id)>0){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }finally {
            BaseDao.release(connection,null,null);
        }

        return flag;
    }
}
