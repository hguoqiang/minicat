package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @description: 封装Response对象，需要借助OutputStream,该对象需要提供核心方法，输出HTML
 * @author: huangguoqiang
 * @create: 2022-01-24 21:11
 **/
public class Response {
    private OutputStream outputStream;

    public Response() {
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 输出字符串
     *
     * @param context
     */
    public void output(String context) throws IOException {
        outputStream.write(context.getBytes());
    }

    /**
     * @param path 根据请求的url，获取到资源的绝对路径，进一步根据绝对路径读取该静态资源文件，最终通过输出流输出
     */
    public void outputHtml(String path) throws IOException {

        //获取资源的绝对路径
        String absolutePath = StaticResourceUtil.getAbsolutePath(path);

        File file = new File(absolutePath);
        if (file.exists() && file.isFile()) {
            //输出静态资源
            StaticResourceUtil.outputStaticResource(new FileInputStream(file), outputStream);
        } else {
            //输出404
            output(HttpProtocolUtil.getHttpHeader404());
        }
    }
}
