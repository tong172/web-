package com.tong.service.Role.impl;

import com.tong.dao.BaseDao;
import com.tong.dao.Role.RoleDao;
import com.tong.dao.Role.impl.RoleDaoImpl;
import com.tong.pojo.Role;
import com.tong.service.Role.RoleService;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao;

    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList(){
        Connection connection = null;
        List<Role> roleList = null;

            try {
                connection = BaseDao.getConnection();
                roleList = roleDao.getRoleList(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
            BaseDao.release(connection,null,null);
        }

        return roleList;
    }
}
