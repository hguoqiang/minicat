package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: Minicat的主类
 * @author: huangguoqiang
 * @create: 2022-01-24 17:16
 **/
public class BootStrap2 {
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
    private void start() throws IOException {
        /*
        V2.0需求：封装Request和Response对象，返回html静态资源⽂件
        */
        //监听端口
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("========minicat start on port:" + port);

        while (true) {
            //接收请求，会阻塞，等待客户端连接过来
            Socket accept = serverSocket.accept();
            System.out.println("接收到请求");
            //接收到请求，获取其输入流，从输入流获取请求信息
            InputStream inputStream = accept.getInputStream();

           /* //数据长度
            int available = 0;
            while (available == 0) {
                available = inputStream.available();
            }

            byte[] bytes = new byte[available];

            inputStream.read(bytes);
            System.out.println("请求信息：" + new String(bytes));*/

            //封装Request 和 Response
            Request request = new Request(inputStream);
            Response response = new Response(accept.getOutputStream());
            response.outputHtml(request.getUrl());

            accept.close();
        }

    }

    /**
     * minicat的程序入口
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {

        BootStrap2 bootStrap = new BootStrap2();
        bootStrap.start();
    }
}
