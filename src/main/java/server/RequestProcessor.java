package server;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-01-25 16:26
 **/
public class RequestProcessor extends Thread {

    private Socket accept;
    private Map<String, HttpServlet> servletMap;

    public RequestProcessor(Socket accept, Map<String, HttpServlet> servletMap) {
        this.accept = accept;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
