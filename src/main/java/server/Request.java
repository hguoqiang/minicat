package server;


import java.io.IOException;
import java.io.InputStream;

/**
 * @description: 把请求信息封装为Request对象（根据InputSteam输⼊流封装）
 * @author: huangguoqiang
 * @create: 2022-01-24 20:56
 **/
public class Request {
    private String method; // 请求⽅式，⽐如GET/POST
    private String url; // 例如 /,/index.html
    private InputStream inputStream; // 输⼊流，其他属性从输⼊流中解析出来

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Request() {
    }

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;


        //从输入流获取请求信息
        int available = 0;
        while (available == 0) {
            available = inputStream.available();
        }
        byte [] bytes = new byte[available];
        inputStream.read(bytes);

        String inputStr = new String(bytes);

        //获取第一行请求信息
        String firstLine = inputStr.split("\\n")[0];
        String[] s = firstLine.split(" ");
        this.method = s[0];
        this.url = s[1];
        System.out.println("method:"+method);
        System.out.println("url:"+url);


    }
}
