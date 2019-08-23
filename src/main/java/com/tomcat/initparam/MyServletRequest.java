package com.tomcat.initparam;

import java.io.*;

/**
 * Created by zjb on 2019/8/23.
 * 自定义servlet，相当于HttpServletRequest
 */
public class MyServletRequest {
    //     请求方式
    private String method;
    //    请求URL
    private String url;
    //   请求参数
    private String[] paramArray;


    public MyServletRequest(InputStream in) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        try {
            String[] split = bufferedReader.readLine().split(" ");
            if (split.length == 3) {
                this.method = split[0];
                String allUrl = split[1];
                if (allUrl.contains("?")) {
                    this.url = allUrl.substring(0, allUrl.indexOf("?"));
                    this.paramArray = allUrl.substring(allUrl.indexOf("?") + 1).split("&");
                } else {
                    this.url = allUrl;
                }

                if (allUrl.endsWith("ico")) return;
                // 注：split[2] 是 协议：HTTP/1.1
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String[] getParamArray() {
        return paramArray;
    }
}
