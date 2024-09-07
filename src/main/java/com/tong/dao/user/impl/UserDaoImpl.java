package com.tong.dao.user.impl;

import com.mysql.cj.util.StringUtils;
import com.tong.dao.BaseDao;
import com.tong.dao.user.UserDao;
import com.tong.pojo.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    // 得到要登录的用户
    public User getLoginUser(Connection connection, String userCode) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        User user = null;
        if (connection!=null){
            String sql = "select * from smbms.smbms_user where userCode=?";
            Object[] params = {userCode};
            try {
                rs = BaseDao.execute(connection, sql, params, rs, statement);
                if (rs.next()){
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getDate("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getDate("modifyDate"));
                    user.setUserRole(rs.getInt("userRole"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                BaseDao.release(null, statement, rs);
            }
        }

        return user;
    }

    @Override
    // 修改当前用户的密码
    public String updatePwd(Connection connection, int id, String password) throws SQLException {

        PreparedStatement pstm = null;
        String update = null;

        if (connection!=null){
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object[] params = {password,id};
            update = String.valueOf(BaseDao.update(connection, sql, params, pstm));
            BaseDao.release(null,pstm,null);
        }

        return update;
    }
    //根据用户名或角色查询用户总数
    @Override
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;

        if (connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u, smbms_role r where u.userRole=r.id");

            ArrayList<Object> list = new ArrayList<>();

            if (!StringUtils.isNullOrEmpty(username)){
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%");
            }
            if (userRole>0){
                sql.append(" and u.userRole like ?");
                list.add(userRole);
            }
            //将list转为数组
            Object[] objects = list.toArray();
            System.out.println("UserDaoImpl===>getUserCount==>"+sql.toString());
            rs = BaseDao.execute(connection, sql.toString(), objects, rs, pstm);

            if (rs.next()){
                count = rs.getInt("count");
            }
            BaseDao.release(null, null,rs);
        }

        return count;
    }

    //获取用户列表并分页
    @Override
    public List<User> getUserList(Connection connection, String username, int userRole, int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<User> userList = new ArrayList<>();
        int count = 0;

        if (connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");

            ArrayList<Object> list = new ArrayList<>();

            if (!StringUtils.isNullOrEmpty(username)){
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%");
            }
            if (userRole>0){
                sql.append(" and u.userRole like ?");
                list.add(userRole);
            }
            sql.append(" order by u.creationDate DESC limit ?,?");
            System.out.println(sql);
            //分页：limit startIndex，pageSize；总数
            //当前页：(当前页-1)*pageSize
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);
            //将list转为数组
            Object[] objects = list.toArray();
            System.out.println("getUserList==>"+sql.toString());
            rs = BaseDao.execute(connection, sql.toString(), objects, rs, pstm);

            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));

                userList.add(user);
            }

            BaseDao.release(null, null,rs);
        }
        return userList;

    }

    @Override
    public int add(Connection connection, User user) throws SQLException {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if (connection!=null){
            String sql = "insert into smbms_user(userCode, userName, userPassword,userRole, gender, birthday, phone, address, creationDate, createdBy) VALUES (?,?,?,?,?,?,?,?,?,?)";
            System.out.println("add=====>"+sql.toString());
            Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),
                    user.getUserRole(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getCreationDate(),user.getCreatedBy()};
            BaseDao.update(connection,sql,params,pstm);
            BaseDao.release(null,pstm,null);
        }

        return updateRows;
    }

    //修改用户信息


    @Override
    public int modify(Connection connection, User user) throws SQLException {
        PreparedStatement pstm = null;
        int flag = 0;
        if (connection!=null){
            String sql = "update smbms_user set userName=?,gender=?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id=?";
            Object[] params = {user.getUserName(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getUserRole(),user.getModifyBy(),
                    user.getModifyDate(),user.getId()};
            flag = BaseDao.update(connection, sql, params, pstm);
            BaseDao.release(null,pstm,null);
        }
        return flag;
    }

    @Override
    public User getUserById(Connection connection, String id) throws Exception {
        User user = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if(null != connection){
            String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=? and u.userRole = r.id";
            Object[] params = {id};
            rs = BaseDao.execute(connection,sql,params,rs,pstm);
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
                user.setUserRoleName(rs.getString("userRoleName"));
            }
            BaseDao.release(null, pstm, rs);
        }
        return user;
    }

    @Override
    public int deleteUser(Connection connection, int id) throws SQLException {
        PreparedStatement pstm = null;
        int delUserNum = 0;
        String sql = "delete from smbms_user where id=?";
        Object[] params = {id};
        if (connection!=null){
            delUserNum = BaseDao.update(connection, sql, params, pstm);
            BaseDao.release(null,pstm,null);
        }
        return delUserNum;
    }
}
