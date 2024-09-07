package com.tong.dao.Role.impl;

import com.tong.dao.BaseDao;
import com.tong.dao.Role.RoleDao;
import com.tong.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        List<Role> roleList = new ArrayList<>();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if (connection!=null){
            String sql = "select * from smbms_role";
            Object[] params = {};
            rs = BaseDao.execute(connection, sql, params, rs, pstm);

            while (rs.next()){
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                roleList.add(role);
            }
            BaseDao.release(null,pstm,rs);
        }

        return roleList;
    }
}
