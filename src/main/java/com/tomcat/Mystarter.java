package com.tomcat;

import com.tomcat.process.MyProcess;
import com.tomcat.servlet.MyServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zjb on 2019/8/23.
 */
public class Mystarter {
    //    自定义端口
    private static final Integer PORT = 8081;

    //    URL-servlet 映射
    public static Map<String, Object> servletMapping = new HashMap<String, Object>();

    //    容器启动入口
    public static void main(String[] args) {
        System.out.println("程序开始启动！");
        System.out.println("程序开始初始化！");
        //创建服务端口
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            //初始化加载web.xml
            init();
            //循环接收客户端请求
            do {
                // 开启新线程处理请求
                Socket socket = serverSocket.accept();
                Thread thread = new MyProcess(socket);
                thread.start();
            } while (Boolean.TRUE);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载web.xml
     */
    private static void init() {
        InputStream resourceAsStream = Mystarter.class.getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.elements();
            for (int i = 0, length = elements.size(); i < length; i++) {
                Element element = elements.get(i);
                List<Element> es = element.elements();
                for (int j = 0, lgth = es.size(); j < lgth; j++) {
                    Element element2 = es.get(j);
                    String ename1 = element2.getName().toString();
                    if ("servlet-name".equals(ename1) && "servlet".equals(element.getName().toString())) {
                        String servletName = element2.getStringValue();
                        Element ele2 = element.element("servlet-class");
                        String classname = ele2.getStringValue();
                        List<Element> elements2 = rootElement.elements("servlet-mapping");
                        for (int k = 0, lk = elements2.size(); k < lk; k++) {
                            Element element4 = elements2.get(k);
                            List<Element> es3 = element4.elements();
                            for (int op = 0, opp = es3.size(); op < opp; op++) {
                                if ("servlet-name".equals(es3.get(op).getName().toString())
                                        && servletName.equals(es3.get(op).getStringValue())) {
                                    Element element7 = element4.element("url-pattern");
                                    String urlPattern = element7.getStringValue();
                                    servletMapping.put(urlPattern, (MyServlet) Class.forName(classname).newInstance());
                                    System.out.println("==> 加载 " + classname + ":" + urlPattern);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != resourceAsStream) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
