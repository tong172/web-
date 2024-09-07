package com.tong.service.bill;

import com.tong.pojo.Bill;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

public interface BillService {
    //展示订单信息
    public List<Bill> getBillList(Bill bill);

    //增加订单信息
    public boolean add(Bill bill);

    //修改订单信息
    public boolean modify(Bill bill);

    //删除订单信息
    public boolean deleteBillById(String delId);

    //根据id查询订单
    public Bill getBillById(int id);
}
