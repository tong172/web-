package com.tong.dao.bill;

import com.tong.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BillDao {
    //展示订单列表
    public List<Bill> getBillList(Connection connection,Bill bill) throws SQLException;
    //通过订单id获取订单
    public Bill getBillById(Connection connection,int id) throws Exception;
    //增加订单
    public int add(Connection connection, Bill bill) throws SQLException;
    //修改订单
    public int modify(Connection connection,Bill bill) throws Exception;

    //删除订单信息
    public int deleteBillById(Connection connection,String delId) throws Exception;

    //通过供应商id查询订单数量，用于在删除供应商表的数据时，进行判断是否可以删除
    public int getBillCountByProviderId(Connection connection, String providerId) throws Exception;
}
