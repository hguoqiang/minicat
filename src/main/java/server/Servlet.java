package server;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-01-25 14:09
 **/
public interface Servlet {
    void init() throws Exception;

    void destroy() throws Exception;

    void service(Request request,Response response) throws Exception;



}
