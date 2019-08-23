package com.tomcat.process;

import com.tomcat.Mystarter;
import com.tomcat.initparam.MyServletRequest;
import com.tomcat.initparam.MyServletResponse;
import com.tomcat.servlet.MyServlet;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zjb on 2019/8/23.
 * 自定义的Tomcat内核处理器
 */
public class MyProcess extends Thread {

    private static final String SUCCESS = "200";
    private static final String NOT_FOUND = "404";

    private Socket socket;
    private String address;
    private Integer port;
    private String status;
    private String url;

    public MyProcess(Socket socket) {
        this.socket = socket;
        InetAddress inetAddress = socket.getInetAddress();
        this.address = inetAddress.getHostAddress();
        this.port = socket.getLocalPort();
    }

    @Override
    public void run() {
//        接收请求，封装request
        try {
            MyServletRequest request = new MyServletRequest(socket.getInputStream());
//            自定义response进行封装
            MyServletResponse response = new MyServletResponse(socket.getOutputStream());
            String url = request.getUrl();
//            通过URL匹配Servlet(这里还可以使用正则匹配)
            MyServlet servlet = (MyServlet) Mystarter.servletMapping.get(url);
            if (null != servlet) {
                this.status = SUCCESS;
                servlet.service(request, response);
            } else {
                //容器中不存在请求的Servlet
                this.status = NOT_FOUND;
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(new String(MyServletResponse.RESPONSE_HEADER + "welcome! error : Cannot find the servlet!").getBytes("utf8"));
                outputStream.flush();
                outputStream.close();
            }
            if (!"/favicon.ico".equals(url)) {
                // 简单记录我们自己的访问日志
                logRecord();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != socket) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 记录日志
     */
    private void logRecord() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        String dateStr = dateFormat.format(date);
        String record = dateStr + " " + this.address + ":" + this.port
                + " ==> " + this.url + " , response:" + this.status + "\r\n";
        File logFile = new File("E:\\demos\\MyTomcat\\log.log");
        try {
            if (!logFile.exists()) logFile.createNewFile();
            Writer writer = new BufferedWriter(new FileWriter(logFile));
            writer.append(record);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
