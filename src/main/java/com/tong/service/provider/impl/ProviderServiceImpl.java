package com.tong.service.provider.impl;

import com.tong.dao.BaseDao;
import com.tong.dao.bill.BillDao;
import com.tong.dao.bill.impl.BillDaoImpl;
import com.tong.dao.provider.ProviderDao;
import com.tong.dao.provider.impl.ProviderDaoImpl;

import com.tong.pojo.Provider;
import com.tong.service.provider.ProviderService;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProviderServiceImpl implements ProviderService {
    private ProviderDao providerDao;
    private BillDao billDao;
    public ProviderServiceImpl(){
        providerDao = new ProviderDaoImpl();
        billDao = new BillDaoImpl();
    }
    @Override
    public List<Provider> getProviderList(String proName, String proCode) {
        Connection connection = null;
        List<Provider> providerList = null;
        try {
            connection = BaseDao.getConnection();
            providerList = providerDao.getProviderList(connection,proName,proCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.release(connection,null,null);
        }
        return providerList;
    }

    @Override
    public boolean add(Provider provider){
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if (providerDao.add(connection,provider)>0){
                flag = true;
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.release(connection,null,null);
        }
        return flag;
    }

    /**
     * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
     * 若订单表中无该供应商的订单数据，则可以删除
     * 若有该供应商的订单数据，则不可以删除
     * 返回值billCount
     * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
     * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
     *
     * ---判断
     * 如果billCount = -1 失败
     * 若billCount >= 0 成功
     */
    @Override
    public int deleteProviderById(String delId) {
        Connection connection = null;
        int billCount = -1;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            try {
                billCount = billDao.getBillCountByProviderId(connection,delId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (billCount==0){
                try {
                    providerDao.deleteProviderById(connection,delId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            billCount = -1;
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally {
            BaseDao.release(connection,null,null);
        }
        return billCount;
    }

    //修改供应商信息

    @Override
    public boolean modify(Provider provider) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (providerDao.modify(connection,provider)>0){
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.release(connection,null,null);
        }
        return flag;
    }

    @Override
    public Provider getProviderById(String id) {
        Provider provider = null;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            provider = providerDao.getProviderById(connection,id);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.release(connection,null,null);
        }
        return provider;
    }
}
