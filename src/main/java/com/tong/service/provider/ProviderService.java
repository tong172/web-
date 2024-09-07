package com.tong.service.provider;

import com.tong.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProviderService {
    public List<Provider> getProviderList(String proName, String proCode);

    public boolean add(Provider provider);

    public int deleteProviderById(String delId);

    public boolean modify(Provider provider);

    public Provider getProviderById(String id);
}
