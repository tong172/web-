package com.tong.service.Role;

import com.tong.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleService {
    //获取角色列表
    public List<Role> getRoleList();

}
