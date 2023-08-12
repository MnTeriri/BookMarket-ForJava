package com.example.bookmarket.job;

import com.example.bookmarket.dao.IOrderDao;
import com.example.bookmarket.model.Order;
import com.example.bookmarket.service.IOrderService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderJob implements Job {
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IOrderService orderService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String oid = jobDataMap.getString("orderOid");//获取创建订单的订单号
        Order order = orderService.searchOrder(oid);
        if (order.getStatus() == 0) {//如果订单未付款
            Map<String, Object> data = new HashMap<>();
            data.put("oid", order.getOid());
            data.put("status", 4);
            orderDao.cancelOrder(data);//取消订单
            Integer result = (Integer) data.get("result");
            if (result == 1) {
                System.out.println("当前时间：" + jobExecutionContext.getFireTime() + "，订单：" + oid + "付款超时，被取消！");
            } else {
                System.out.println("取消订单执行出错！");
            }
        } else {
            System.out.println("订单已付款或已取消！");
        }
    }
}
