package com.tong.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.tong.pojo.Provider;
import com.tong.pojo.User;
import com.tong.service.provider.ProviderService;
import com.tong.service.provider.impl.ProviderServiceImpl;
import com.tong.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method!=null&& method.equals("query")){
            this.query(req, resp);
        }else if (method!=null&&method.equals("add")){
            this.add(req,resp);
        }else if (method!=null&&method.equals("delprovider")){
            this.delProvider(req,resp);
        }else if (method!=null&&method.equals("modifysave")){
            this.modify(req,resp);
        }else if (method!=null&&method.equals("modify")){
            this.getProviderById(req,resp,"providermodify.jsp");
        }else if (method!=null&&method.equals("view")){
            this.getProviderById(req,resp,"providerview.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void query(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String queryProName = request.getParameter("queryProName");
        String queryProCode = request.getParameter("queryProCode");
        if (!StringUtils.isNullOrEmpty(queryProName)){
            queryProName = "";
        }
        if (!StringUtils.isNullOrEmpty(queryProCode)){
            queryProCode = "";
        }
        List<Provider> providerList = new ArrayList<>();
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList(queryProName,queryProCode);
        request.setAttribute("providerList",providerList);
        request.setAttribute("queryProName",queryProName);
        request.setAttribute("queryProCode",queryProCode);
        request.getRequestDispatcher("providerlist.jsp").forward(request,response);
    }

    public void add(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        String proCode = request.getParameter("proCode");
        String proName = request.getParameter("proName");
        String proContact = request.getParameter("proContact");
        String proPhone = request.getParameter("proPhone");
        String proAddress = request.getParameter("proAddress");
        String proFax = request.getParameter("proFax");
        String proDesc = request.getParameter("proDesc");

        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());

        boolean flag = false;
        ProviderService providerService = new ProviderServiceImpl();
        flag = providerService.add(provider);
        if (flag){
            response.sendRedirect(request.getContextPath()+"/jsp/provider.do?method=query");
        }else {
            request.getRequestDispatcher("provideradd.jsp");
        }
    }

    public void delProvider(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
        String id = request.getParameter("proid");
        HashMap<Object, Object> resultMap = new HashMap<>();
        if (!StringUtils.isNullOrEmpty(id)){
            ProviderService providerService = new ProviderServiceImpl();
            int flag = providerService.deleteProviderById(id);
            if (flag == 0){
                resultMap.put("delResult","true");
            }else if (flag == -1){
                resultMap.put("delResult","false");
            }else if (flag >0){
                resultMap.put("delResult",String.valueOf(flag));
            }
        }else {
            resultMap.put("delResult","notexit");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write(JSONArray.toJSONString(resultMap));
        out.flush();
        out.close();
    }

    public void modify(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String proName = request.getParameter("proName");
        String proContact = request.getParameter("proContact");
        String proPhone = request.getParameter("proPhone");
        String proAddress = request.getParameter("proAddress");
        String proFax = request.getParameter("proFax");
        String proDesc = request.getParameter("proDesc");
        String id = request.getParameter("id");

        Provider provider = new Provider();

        provider.setId(Integer.parseInt(id));
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());

        boolean flag = false;
        ProviderService providerService = new ProviderServiceImpl();
        flag = providerService.modify(provider);
        System.out.println("------------------->"+flag);
        if (flag){
            response.sendRedirect(request.getContextPath()+"/jsp/provider.do?method=query");
        }else {
            request.getRequestDispatcher("providermodify.jsp");
        }

    }

    public void getProviderById(HttpServletRequest request,HttpServletResponse response,String url) throws ServletException, IOException {
        String id = request.getParameter("proid");
        if (!StringUtils.isNullOrEmpty(id)){
            ProviderServiceImpl providerService = new ProviderServiceImpl();
            Provider provider = null;
            provider = providerService.getProviderById(id);
            request.setAttribute("provider",provider);
            request.getRequestDispatcher(url).forward(request,response);

        }
    }
}
