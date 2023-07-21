package com.example.bookmarket.util;//package util;
//
//import com.alibaba.fastjson2.JSON;
//import model.Book;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
//import java.util.ArrayList;
//
//public class JedisUtils {
//    private static final JedisPool pool;
//    static {
//        JedisPoolConfig config=new JedisPoolConfig();
//        config.setMaxIdle(10);//最大空闲连接数
//        config.setMaxTotal(30);//最大连接数
//        pool = new JedisPool(config, "127.0.0.1", 6379, 2000);
//    }
//
//    public static Jedis getConnection(){
//        return pool.getResource();
//    }
//
//    public static void close(Jedis jedis){
//        jedis.close();
//    }
//
//
//    /**
//     * @param list 数据
//     * @param database Redis的哪个数据库（0 Book）
//     * */
//    public static <T> void saveDataToRedis(ArrayList<T> list,Class<T> clazz,int database){
//        Jedis jedis = getConnection();
//        jedis.select(database);
//        for (int i = 0; i < list.size(); i++) {
//            String key="";
//            String json= JSON.toJSONString(list.get(i));
//            if (clazz== Book.class){
//                key=((Book)list.get(i)).getBid();
//            }
//            jedis.set(key,json);
//        }
//        JedisUtils.close(jedis);
//    }
//}
