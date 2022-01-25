package server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: Minicat的主类
 * @author: huangguoqiang
 * @create: 2022-01-24 17:16
 **/
public class BootStrap3 {

    private Map<String, HttpServlet> servletMap = new HashMap<>();
    /**
     * 定义socket监听的端口号
     */
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 封装minicat启动时候初始化一些操作，最重要的就是监听端口，等待请求
     */
    private void start() throws Exception {
        /*
        V3.0需求：可以请求动态资源（Servlet）
        */

        //加载解析配置文件web.xml，
        loadServlet();

        //监听端口
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("========minicat start on port:" + port);

        while (true) {
            //接收请求，会阻塞，等待客户端连接过来
            Socket accept = serverSocket.accept();
            System.out.println("接收到请求");
            //接收到请求，获取其输入流，从输入流获取请求信息
            InputStream inputStream = accept.getInputStream();


            //封装Request 和 Response
            Request request = new Request(inputStream);
            Response response = new Response(accept.getOutputStream());

            if (servletMap.get(request.getUrl()) == null) {
                //就是访问静态资源

                response.outputHtml(request.getUrl());
            } else {
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request, response);
            }

            accept.close();
        }

    }

    /**
     * 加载解析配置文件web.xml，初始化servlet
     */
    private void loadServlet() throws DocumentException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");

        SAXReader reader = new SAXReader();

        Document document = reader.read(inputStream);

        //根元素
        Element rootElement = document.getRootElement();

        //从根元素下获取所有的 servlet 标签
        List<Element> servletNodes = rootElement.selectNodes("//servlet");

        for (int i = 0; i < servletNodes.size(); i++) {
            Element servletNode = servletNodes.get(i);
            Element servletNameNode = (Element) servletNode.selectSingleNode("servlet-name");
            String servletName = servletNameNode.getStringValue();
            Element servletClassNode = (Element) servletNode.selectSingleNode("servlet-class");
            String servletClass = servletClassNode.getStringValue();

            //根据servlet-name 找到 url-pattern，查询 servlet-mapping 条件是servlet-name 等于上面的 servletName
            Element servletMappingNode = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
            Element urlPatternNode = (Element) servletMappingNode.selectSingleNode("url-pattern");
            String urlPattern = urlPatternNode.getStringValue();

            servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());

        }


    }

    /**
     * minicat的程序入口
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

        BootStrap3 bootStrap = new BootStrap3();
        bootStrap.start();
    }
}
