package com.tong.service.bill.impl;

import com.tong.dao.BaseDao;
import com.tong.dao.bill.BillDao;
import com.tong.dao.bill.impl.BillDaoImpl;
import com.tong.pojo.Bill;
import com.tong.service.bill.BillService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImpl implements BillService {
    private BillDao billDao;

    public BillServiceImpl() {
        billDao = new BillDaoImpl();
    }

    @Override
    public Bill getBillById(int id) {
        Bill bill = null;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            bill = billDao.getBillById(connection,id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.release(connection,null,null);
        }
        return bill;
    }

    @Override
    public List<Bill> getBillList(Bill bill) {
        Connection connection = null;
        List<Bill> billList = null;
        try {
            connection = BaseDao.getConnection();
            billList = billDao.getBillList(connection,bill);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.release(connection,null,null);
        }
        return billList;
    }

    @Override
    public boolean add(Bill bill) {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if (billDao.add(connection,bill)>0){
                flag = true;
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("======发生错误，回滚事务======");
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
    public boolean modify(Bill bill) {
        boolean flag = false;
        Connection connection = null;

        try {
            connection = BaseDao.getConnection();
            if (billDao.modify(connection,bill)>0){
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
    public boolean deleteBillById(String delId) {
        boolean flag = false;
        Connection connection = null;

        try {
            connection = BaseDao.getConnection();
            if (billDao.deleteBillById(connection,delId)>0){
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.release(connection,null,null);
        }
        return flag;
    }

}
