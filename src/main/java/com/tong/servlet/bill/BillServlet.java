package com.tong.servlet.bill;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.tong.pojo.Bill;
import com.tong.pojo.Provider;
import com.tong.pojo.User;
import com.tong.service.bill.BillService;
import com.tong.service.bill.impl.BillServiceImpl;
import com.tong.service.provider.ProviderService;
import com.tong.service.provider.impl.ProviderServiceImpl;
import com.tong.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method!=null&&method.equals("query")){
            this.query(req,resp);
        }else if (method!=null&&method.equals("add")){
            this.add(req,resp);
        }else if (method!=null&&method.equals("getproviderlist")){
            this.getProviderList(req, resp);
        }else if (method!=null&&method.equals("modifysave")){
            this.modify(req,resp);
        }else if (method!=null&&method.equals("modify")){
            this.getBillById(req,resp,"billmodify.jsp");
        }else if (method!=null&&method.equals("view")){
            this.getBillById(req,resp,"billview.jsp");
        }else if (method!=null&&method.equals("delbill")){
            this.deleteBillById(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void query(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        List<Provider> providerList = new ArrayList<>();
        ProviderService providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList("","");
        request.setAttribute("providerList",providerList);

        String queryProductName = request.getParameter("queryProductName");
        String queryProviderId = request.getParameter("queryProviderId");
        String queryIsPayment = request.getParameter("queryIsPayment");

        if (StringUtils.isNullOrEmpty(queryProductName)){
            queryProductName = "";
        }
        List<Bill> billList = new ArrayList<>();
        BillService billService = new BillServiceImpl();
        Bill bill = new Bill();
        if (StringUtils.isNullOrEmpty(queryProviderId)){
            bill.setProviderId(0);
        }else {
            bill.setProviderId(Integer.parseInt(queryProviderId));
        }
        if (StringUtils.isNullOrEmpty(queryIsPayment)){
            bill.setIsPayment(0);
        }else {
            bill.setIsPayment(Integer.parseInt(queryIsPayment));
        }
        bill.setProductName(queryProductName);

        billList = billService.getBillList(bill);
        request.setAttribute("billList",billList);
        request.setAttribute("queryProductName", queryProductName);
        request.setAttribute("queryProviderId", queryProviderId);
        request.setAttribute("queryIsPayment", queryIsPayment);

        request.getRequestDispatcher("billlist.jsp").forward(request,response);
    }

    public void add(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String billCode = request.getParameter("billCode");
        String productName = request.getParameter("productName");
        String productDesc = request.getParameter("productDesc");
        String productUnit = request.getParameter("productUnit");

        String productCount = request.getParameter("productCount");
        String totalPrice = request.getParameter("totalPrice");
        String providerId = request.getParameter("providerId");
        String isPayment = request.getParameter("isPayment");

        Bill bill = new Bill();

        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());

        boolean flag = false;
        BillServiceImpl billService = new BillServiceImpl();
        flag = billService.add(bill);
        if (flag){
            response.sendRedirect(request.getContextPath()+"/jsp/bill.do?method=query");
        }else {
            request.getRequestDispatcher("billadd.jsp").forward(request,response);
        }
    }

    public void modify(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("id");
        String productName = request.getParameter("productName");
        String productDesc = request.getParameter("productDesc");
        String productUnit = request.getParameter("productUnit");
        String productCount = request.getParameter("productCount");
        String totalPrice = request.getParameter("totalPrice");
        String providerId = request.getParameter("providerId");
        String isPayment = request.getParameter("isPayment");

        Bill bill = new Bill();

        bill.setId(Integer.parseInt(id));
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());

        boolean flag = false;
        BillServiceImpl billService = new BillServiceImpl();
        flag = billService.modify(bill);
        if (flag){
            response.sendRedirect(request.getContextPath()+"/jsp/bill.do?method=query");
        }else {
            request.getRequestDispatcher("billmodify.jsp").forward(request,response);
        }
    }

    public void deleteBillById(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String id = request.getParameter("billid");
        HashMap<String,String> resultMap = new HashMap<>();
        if (!StringUtils.isNullOrEmpty(id)){
            BillServiceImpl billService = new BillServiceImpl();
            boolean flag = billService.deleteBillById(id);
            if (flag){
                resultMap.put("delResult","true");
            }else {
                resultMap.put("delResult","false");
            }
        }else {
            resultMap.put("delResult","notexit");
        }

        //把resultMap转换为json对象输出
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write(JSONArray.toJSONString(resultMap));
        out.flush();
        out.close();
    }

    public void getProviderList(HttpServletRequest request,HttpServletResponse response) throws IOException {
        List<Provider> providerList = new ArrayList<>();
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList("","");

        //把providerList转换为json对象输出
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write(JSONArray.toJSONString(providerList));
        out.flush();
        out.close();
    }

    public void getBillById(HttpServletRequest request,HttpServletResponse response,String url) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("billid"));
        if (!StringUtils.isNullOrEmpty(String.valueOf(id))){
            BillServiceImpl billService = new BillServiceImpl();
            Bill bill = new Bill();
            bill = billService.getBillById(id);
            request.setAttribute("bill",bill);
            request.getRequestDispatcher(url).forward(request,response);
        }
    }

}



