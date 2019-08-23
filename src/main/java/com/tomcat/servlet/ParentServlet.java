package com.tomcat.servlet;

import com.tomcat.initparam.MyServletRequest;
import com.tomcat.initparam.MyServletResponse;

/**
 * Created by zjb on 2019/8/23.
 * 自定义容器servlet超级父类
 */
public abstract class ParentServlet {

    public void service(MyServletRequest request, MyServletResponse response) {
        if ("GET".equals(request.getMethod())) doGet(request, response);
        if ("POST".equals(request.getMethod())) doPost(request, response);
    }

    public abstract void doGet(MyServletRequest request, MyServletResponse response);

    public abstract void doPost(MyServletRequest request, MyServletResponse response);

}
