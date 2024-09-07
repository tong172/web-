package com.tong.dao.provider;

import com.tong.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProviderDao {
    public List<Provider> getProviderList(Connection connection,String proName, String proCode) throws SQLException;

    public int add(Connection connection,Provider provider) throws SQLException;

    public int deleteProviderById(Connection connection,String delId) throws Exception;

    public int modify(Connection connection,Provider provider) throws Exception;

    public Provider getProviderById(Connection connection,String id) throws SQLException;
}
