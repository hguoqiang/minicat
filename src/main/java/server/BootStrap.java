package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: Minicat的主类
 * @author: huangguoqiang
 * @create: 2022-01-24 17:16
 **/
public class BootStrap {
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
        V1.0需求：浏览器请求http://localhost:8080,返回⼀个固定的字符串到⻚⾯"Hello Minicat!"
        */
        //监听端口
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("========minicat start on port:" +port);

        while (true){
            //接收请求，会阻塞，等待客户端连接过来
            Socket accept = serverSocket.accept();
            System.out.println("接收到请求");
            //接收到请求，获取其输出流
            OutputStream outputStream = accept.getOutputStream();
            String text = "Hello Minicat!";
            String resp = HttpProtocolUtil.getHttpHeader200(text.getBytes().length)+text;
            outputStream.write(resp.getBytes());
            accept.close();
        }

    }

    /**
     * minicat的程序入口
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {

        BootStrap bootStrap = new BootStrap();
        bootStrap.start();
    }
}
