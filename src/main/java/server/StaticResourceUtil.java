package server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-01-24 21:20
 **/
public class StaticResourceUtil {
    /**
     * 获取静态资源的绝对路径
     *
     * @param path
     * @return
     */
    public static String getAbsolutePath(String path) {
        URL resource = StaticResourceUtil.class.getResource("/");
        String absolutePath = resource.getPath();
        return absolutePath.replaceAll("\\\\", File.separator) + path;

    }


    /**
     * 读取静态资源⽂件输⼊流，通过输出流输出
     */
    public static void outputStaticResource(InputStream inputStream,
                                            OutputStream outputStream) throws IOException {
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        int resourceSize = count;
        // 输出http请求头
        outputStream.write(HttpProtocolUtil.getHttpHeader200(resourceSize).getBytes());

        // 读取内容输出
        long written = 0;// 已经读取的内容⻓度
        int byteSize = 1024; // 计划每次缓冲的⻓度
        byte[] bytes = new byte[byteSize];
        while (written < resourceSize) {
            if (written + byteSize > resourceSize) { // 说明剩余未读取⼤⼩不⾜⼀个1024⻓度，那就按真实⻓度处理
                byteSize = (int) (resourceSize - written); // 剩余的⽂件内容⻓度
                bytes = new byte[byteSize];
            }
            inputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();
            written += byteSize;
        }
    }


}
