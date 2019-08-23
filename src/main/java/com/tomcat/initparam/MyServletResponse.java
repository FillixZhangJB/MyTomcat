package com.tomcat.initparam;

import java.io.OutputStream;

/**
 * Created by zjb on 2019/8/23.
 * 相当于HttpServletResponse
 */
public class MyServletResponse {
    private OutputStream outputStream;

    //    添加response的响应头
    public static final String RESPONSE_HEADER = "HTTP/1.1 200 \r\n"
            + "Content-Type: text/html\r\n"
            + "\r\n";

    public MyServletResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
