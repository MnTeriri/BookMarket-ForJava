package com.example.bookmarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarket.model.Order;
import com.example.bookmarket.provider.OrderSqlProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IOrderDao extends BaseMapper<Order> {
    @SelectProvider(type = OrderSqlProvider.class, method = "getOrderListSql")
    @Results({
            @Result(property = "oid", column = "oid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "aid", column = "aid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "address", column = "aid", one = @One(select = "com.example.bookmarket.dao.IAddressDao.searchById")),
            @Result(property = "books", column = "oid", many = @Many(select = "com.example.bookmarket.dao.IOrderBookDao.getOrderDetail"))
    })
    public List<Order> getOrderList(@Param("oid") String oid,
                                    @Param("uid") String uid,
                                    @Param("orderFilter") String orderFilter,
                                    @Param("page") Integer page,
                                    @Param("count") Integer count);

    @Select("SELECT COUNT(id) FROM `Order` WHERE oid LIKE CONCAT('%', #{oid}, '%') AND uid=#{uid}")
    public Long getRecordsFiltered(String oid, String uid);

    @Select("SELECT COUNT(id) FROM `Order` WHERE oid LIKE CONCAT('%', #{oid}, '%') AND uid=#{uid} AND status=#{status}")
    public Long getRecordsFilteredByStatus(String oid, String uid, Integer status);

    /**
     * @param data 存储过程输出参数(data["result"])：1成功、-4SQL语句出错、-3购物车为空、-2书籍缺货或者下架、-1书籍数量不够
     */
    @Update("{CALL create_order(#{oid,mode=IN},#{uid,mode=IN},#{aid,mode=IN},#{result, mode=OUT, jdbcType=INTEGER})}")
    @Options(statementType = StatementType.CALLABLE)
    public void createOrder(Map<String, Object> data);

    @Update("{CALL cancel_order(#{oid,mode=IN},#{status, mode=IN},#{result, mode=OUT, jdbcType=INTEGER})}")
    @Options(statementType = StatementType.CALLABLE)
    public void cancelOrder(Map<String, Object> data);
}
