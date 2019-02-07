package cn.stylefeng.guns;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ConnectionHelper {

    public static MemcachedClient getClient() {


        // 连接配置
        // 创建与服务端之间的连接
        MemcachedClient memcachedClient = null;
        try {
            memcachedClient = new XMemcachedClient("106.15.191.27", 8088);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return memcachedClient;
    }


    public static void main(String[] args) throws IOException, InterruptedException, MemcachedException, TimeoutException {



        // 获取操作业务对象
        // 操作业务
        MemcachedClient memcachedClient = getClient();
        String str = "Hello Imooc2";
        String k1 = "k1";
        memcachedClient.set(k1, 3600, str);
        String val = memcachedClient.get(k1);
        System.out.println("value=" + val);

        // 关闭与服务端连接
        memcachedClient.shutdown();
    }
}
