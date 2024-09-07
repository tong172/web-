package com.tong.servlet.user;

import com.tong.pojo.User;
import com.tong.service.user.UserService;
import com.tong.service.user.impl.UserServiceImpl;
import com.tong.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userCode");
        String password = req.getParameter("userPassword");

        UserService userService = new UserServiceImpl();
        User user = userService.login(userName, password);

        if (user!=null && password.equals(user.getUserPassword())){
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            resp.sendRedirect("jsp/frame.jsp");
        }else {
            req.setAttribute("error","用户名或密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doGet(req,resp);
    }
}
