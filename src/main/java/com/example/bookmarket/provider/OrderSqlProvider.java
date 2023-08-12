package com.example.bookmarket.provider;

import org.apache.ibatis.annotations.Param;

public class OrderSqlProvider {
    public String getOrderListSql(@Param("oid") String oid,
                                  @Param("uid") String uid,
                                  @Param("orderFilter") String orderFilter,
                                  @Param("page") Integer page,
                                  @Param("count") Integer count) {
        String sql = "SELECT * FROM `Order` WHERE oid LIKE CONCAT('%', #{oid}, '%') AND uid=#{uid}";
        if (orderFilter.equals("notPay")) {//待支付订单
            sql = sql + " AND status=0";
        } else if (orderFilter.equals("notReceive")) {//待收货订单
            sql = sql + " AND (status=1 OR status=2)";
        } else if (orderFilter.equals("finish")) {//已完成
            sql = sql + " AND (status=3 OR status=6)";
        } else if (orderFilter.equals("cancel")) {//已取消
            sql = sql + " AND (status=4 OR status=5)";
        }
        sql = sql + " ORDER BY created_time DESC LIMIT #{page},#{count}";
        return sql;
    }
}
