package com.tomcat.servlet;

import com.tomcat.initparam.MyServletRequest;
import com.tomcat.initparam.MyServletResponse;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by zjb on 2019/8/23.
 * 自己业务的Servlet
 */
public class MyServlet extends ParentServlet {
    public void doGet(MyServletRequest request, MyServletResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append(MyServletResponse.RESPONSE_HEADER);
        builder.append("--->url: " + request.getUrl());
        builder.append("--->\tmethod: " + request.getMethod());
        String params = "";
        if (null != request.getParamArray() && request.getParamArray().length > 0) {
            String[] paramArray = request.getParamArray();
            for (int i = 0; i < paramArray.length; i++) {
                params += paramArray[i] + ",";
            }
            builder.append(";--->\t Params: << " + params.substring(0, params.length() - 1) + " >>");
        }
        OutputStream outputStream = response.getOutputStream();
        try {
            outputStream.write(builder.toString().getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doPost(MyServletRequest request, MyServletResponse response) {
        doGet(request, response);
    }
}
