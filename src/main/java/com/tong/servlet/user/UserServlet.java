package com.tong.servlet.user;

import com.mysql.cj.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.tong.pojo.Role;
import com.tong.pojo.User;
import com.tong.service.Role.impl.RoleServiceImpl;
import com.tong.service.user.UserService;
import com.tong.service.user.impl.UserServiceImpl;
import com.tong.util.Constants;
import com.tong.util.PageSupport;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// 实现servlet复用
@WebServlet(name = "UserServlet", value = "/UserServlet")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method!=null && method.equals("savepwd")){
            this.updatePwd(request,response);
        }else if (method!=null && method.equals("pwdmodify")){
            this.pwdModify(request,response);
        }else if (method!=null && method.equals("add")) {
            this.add(request, response);
        }else if (method!=null && method.equals("query")){
            this.query(request,response);
        }else if (method!=null && method.equals("getrolelist")){
            this.getRoleList(request,response);
        }else if (method!=null && method.equals("ucexist")){
            this.userCodeExist(request,response);
        }else if (method!=null && method.equals("modifyexe")){
            this.modify(request,response);
        }else if (method!=null && method.equals("modify")){
            this.getUserById(request,response,"usermodify.jsp");
        }else if (method!=null && method.equals("view")){
            this.getUserById(request,response,"userview.jsp");
        }else if (method!=null && method.equals("deluser")){
            this.delUser(request,response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    //修改密码
    public void updatePwd(HttpServletRequest request, HttpServletResponse response){
        Object o = request.getSession().getAttribute(Constants.USER_SESSION);
        String password = request.getParameter("newpassword");

        String flag = null;
        if (o!=null&&!StringUtils.isNullOrEmpty(password)){
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwdById(((User) o).getId(), password);
            if (flag!=null){
                request.setAttribute("message","修改密码成功，请重新登录");
                request.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                request.setAttribute("message","密码修改失败");
            }
        }else {
            request.setAttribute("message","密码不一致");
        }
        try {
            request.getRequestDispatcher("pwdmodify.jsp").forward(request,response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    //验证旧密码,session中有旧密码
    public void pwdModify(HttpServletRequest request, HttpServletResponse response){
        Object o = request.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = request.getParameter("oldpassword");

        // 万能Map
        HashMap<String, String> map = new HashMap<>();
        if (o==null){
            map.put("result","sessionerror");
        }else if (StringUtils.isNullOrEmpty(oldpassword)){
            map.put("result","error");
        }else {
            String userPassword = ((User) o).getUserPassword();
            if (oldpassword.equals(userPassword)){
                map.put("result","true");
            }else {
                map.put("result","false");
            }
        }

        try {
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            //fastJson将对象转为json字符串传给前端
            writer.write(JSONArray.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //查询用户管理列表
    public void query(HttpServletRequest request, HttpServletResponse response){
        //查询用户列表
        //从前端获取数据
        String queryUsername = request.getParameter("queryname");
        String temp = request.getParameter("queryUserRole");
        String pageIndex = request.getParameter("pageIndex");

        //获取用户列表
        UserServiceImpl userService = new UserServiceImpl();
        int pageSize = 5;
        int currentPageNo = 1;
        int queryUserRole = 0;

        if (queryUsername==null){
            queryUsername="";
        }
        if (temp!=null&&!temp.equals("")){
            queryUserRole = Integer.parseInt(temp); //给查询赋值
        }
        if (pageIndex!=null){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //获取用户总数 并分页
        int totalCount = userService.getUserCount(queryUsername, queryUserRole);
        //分页
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();
        //控制首页与尾页
        //如果客户输入的页面小于1，就把他手动变为1
        if (currentPageNo<1){
            currentPageNo = 1;
        //如果客户输入的页面大于最后一页，就让他等于最后一页
        }else if(currentPageNo>totalPageCount){
            currentPageNo = totalPageCount;
        }

        //展示数据列表
        List<User> userList = userService.getUserList(queryUsername, queryUserRole, currentPageNo, pageSize);
        request.setAttribute("userList",userList);

        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        request.setAttribute("roleList",roleList);

        request.setAttribute("totalCount",totalCount);
        request.setAttribute("currentPageNo",currentPageNo);
        request.setAttribute("totalPageCount",totalPageCount);

        //返回前端
        try {
            request.getRequestDispatcher("userlist.jsp").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userCode = request.getParameter("userCode");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());

        UserService userService = new UserServiceImpl();
        if (userService.add(user)){
            response.sendRedirect(request.getContextPath()+ "/jsp/user.do?method=query");
        }else {
            request.getRequestDispatcher("useradd.jsp").forward(request,response);
        }
    }

    public void getRoleList(HttpServletRequest request,HttpServletResponse response) throws IOException {
        List<Role> roleList = null;
        RoleServiceImpl roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write(JSONArray.toJSONString(roleList));
        out.flush();
        out.close();
    }

    private void userCodeExist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取前端输入 的用户编码
        String userCode = request.getParameter("userCode");
        UserServiceImpl userService = new UserServiceImpl();
        User isNullUser = userService.login(userCode, "");
        //将结果存放在map集合中 让Ajax使用
        Map<String, String> resultMap = new HashMap<>();
        //判断是否已经存在这个用户编码
        boolean flag = isNullUser != null;
        if(flag){
            //用户编码存在
            //将信息存入map中
            resultMap.put("userCode","exist");
        }
        //上面已经封装好 现在需要传给Ajax 格式为json 所以我们得转换格式
        response.setContentType("application/json");//将应用的类型变成json
        PrintWriter writer = response.getWriter();
        //JSONArray 阿里巴巴的JSON工具类 用途就是：转换格式
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    public void modify(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("uid");
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setId(Integer.parseInt(id));
        user.setUserName(userName);
        user.setGender(Integer.parseInt(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.parseInt(userRole));
        user.setModifyBy(((User) request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        UserService userService = new UserServiceImpl();
        if (userService.modify(user)){
            response.sendRedirect(request.getContextPath()+"/jsp/user.do?method=query");
        }else {
            request.getRequestDispatcher("usermodify.jsp").forward(request,response);
        }
    }
    public void getUserById(HttpServletRequest request, HttpServletResponse response, String url)
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            UserService userService = new UserServiceImpl();
            User user = userService.getUserById(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    public void delUser(HttpServletRequest request,HttpServletResponse response) throws IOException {
//        String uid = request.getParameter("uid");
//        int id = 0;
//        //将string类型的uid转为int型
//        id = Integer.parseInt(uid);
//
//        HashMap<String, String> delUserList = new HashMap<>();
//        if (id<=0){
//            delUserList.put("delResult","notexist");
//        }else {
//            UserService userService = new UserServiceImpl();
//            if (userService.deleteUserById(id)){
//                delUserList.put("delResult","true");
//            }else {
//                delUserList.put("delResult","false");
//            }
//        }

        String id = request.getParameter("uid");
        int delId = 0;
        try {
            delId= Integer.parseInt(id);
        }catch (Exception e){
            e.printStackTrace();
            delId = 0;
        }

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (delId <= 0) {
            resultMap.put("delResult", "notexist");
        } else {
            UserService userService = new UserServiceImpl();
            if (userService.deleteUserById(delId)) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
        }

        //把delUserList转为json输出
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write(JSONArray.toJSONString(resultMap));
        out.flush();
        out.close();

    }
}


