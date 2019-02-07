package cn.stylefeng.guns.user;

import cn.stylefeng.guns.ConnectionHelper;
import cn.stylefeng.guns.user.vo.UserModel;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;

public class ShowApi {

    // 新增【set,add】
    public static void showAddAndSet(UserModel um) throws Exception {

        MemcachedClient memcachedClient = ConnectionHelper.getClient();
        memcachedClient.set("set-user:" + um.getUuid(), 3600, um);
        memcachedClient.add("add-user:" + um.getUuid(), 3600, um);
    }


    // 修改【replace,append,prepend】
    public static void showUpdate(UserModel um) throws Exception {

        MemcachedClient memcachedClient = ConnectionHelper.getClient();
        memcachedClient.replace("set-user:" + um.getUuid(), 3600, um);

        // Hi! imooc smniuhe
        memcachedClient.prepend("k1", "Hi!");
        memcachedClient.append("k1", "smniuhe");
    }

    // 删除【del】

    // 查询【get/gets】
    public static void showQuery() throws Exception {

        MemcachedClient memcachedClient = ConnectionHelper.getClient();
        String value = memcachedClient.get("k1");
        System.out.println("value:" + value);
    }

    public static GetsResponse<UserModel> showGets(String key) throws Exception {

        MemcachedClient memcachedClient = ConnectionHelper.getClient();
        GetsResponse<UserModel> gets = memcachedClient.gets(key);
        return gets;
    }


    public static void showCAS(UserModel um) throws Exception {

        MemcachedClient memcachedClient = ConnectionHelper.getClient();
        String key = "set-user:" + um.getUuid();
        GetsResponse<UserModel> userModelGetsResponse = showGets(key);
        long cas = userModelGetsResponse.getCas();

        boolean isSuccess = memcachedClient.cas(key, 3600, um, cas);
        System.out.println("isSuccess=" + isSuccess);

        // 问题：
        /**
         *
         * 背景：订单来了【10个电脑】，检查库存【11个电脑】，修改库存数量，发货
         * -》order
         *  int num = checkWareHouse(...) -> 11 select
         *  -> 时间差
         * -》update
         *
         * 解决方式：
         *  可以直接 update 关系型数据库记录数据（并判断库存数量>多少）执行，此时有行级锁的概念，其他操作不能执行
         */


    }


    // 数值操作【incr,decr】
    // 检查更新【cas】


    public static void main(String[] args) throws Exception {

        UserModel userModel = new UserModel(1, "i2m2ooc", 28);
        /*showAddAndSet(userModel);
        showQuery();*/

        showCAS(userModel);
    }


}
