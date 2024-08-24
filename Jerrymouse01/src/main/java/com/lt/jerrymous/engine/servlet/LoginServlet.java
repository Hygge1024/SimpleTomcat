package com.lt.jerrymous.engine.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    //用户数据库 users ,模拟
    Map<String,String> users = Map.of(
            "bob","bob123",
            "alice","alice123",
            "root","admin123"
    );

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println("进入了登录程序");
        System.out.println("账号：" + username);
        System.out.println("密码：" + password);
        username = "bob";
        password = "bob123";
        String expectedPassword = users.get(username.toLowerCase());

        if (expectedPassword == null || !expectedPassword.equals(password)) {
            PrintWriter pw = resp.getWriter();
            pw.write("""
                    <h1>Login Failed</h1>
                    <p>Invalid username or password.</p>
                    <p><a href="/">Try again</a></p>
                    """);
            pw.close();
        } else {
            //表示当用户登录成功，用户名保存到会话（session）中
            req.getSession().setAttribute("username", username);
            resp.sendRedirect("/");//将用户重定向到主页。
        }
    }
}
