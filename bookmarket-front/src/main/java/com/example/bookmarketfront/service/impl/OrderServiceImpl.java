package com.example.bookmarketfront.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bookmarketfront.dao.IOrderDao;
import com.example.bookmarketfront.job.OrderJob;
import com.example.bookmarketfront.model.Order;
import com.example.bookmarketfront.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public OrderServiceImpl() {
        log.debug("创建服务层实现对象：OrderServiceImpl");
    }

    @Override
    public Integer createOrder(Order order) {
        String oid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtil.randomNumbers(6);
        while (searchOrder(oid) != null) {//如果生成的订单号存在，则重新生成，直到不存在
            oid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtil.randomNumbers(6);
        }
        order.setOid(oid);

        Map<String, Object> data = new HashMap<>();
        data.put("oid", order.getOid());
        data.put("uid", order.getUid());
        data.put("aid", order.getAid());
        orderDao.createOrder(data);//执行存储过程
        Integer result = (Integer) data.get("result");//获取输出参数

        if (result == 1) {//如果订单创建成功
            // 设置订单定时任务，在15分钟后检查订单状态
            JobDetail jobDetail = JobBuilder.newJob(OrderJob.class)
                    .withIdentity(order.getOid(), "orderGroup")
                    .usingJobData("orderOid", order.getOid())
                    .build();
            LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(10);
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(order.getOid(), "orderGroup")
                    .startAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .build();
            try {
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                scheduler.scheduleJob(jobDetail, trigger);//添加订单定时任务
                log.debug("订单定时任务{}添加成功", scheduler);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public Integer cancelOrder(Order order) {
        try {//删除对应订单的定时任务
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.unscheduleJob(new TriggerKey(order.getOid(), "orderGroup"));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("oid", order.getOid());
        data.put("status", order.getStatus());
        orderDao.cancelOrder(data);
        return (Integer) data.get("result");
    }

    @Override
    public Integer updateOrderStatue(Order order) {
        return null;
    }

    @Override
    public Order searchOrder(String oid) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().eq("oid", oid);
        return orderDao.selectOne(queryWrapper);
    }

    @Override
    public List<Order> getOrderList(String oid, String uid, String orderFilter, Integer page, Integer count) {
        return orderDao.getOrderList(oid, uid, orderFilter, (page - 1) * count, count);
    }

    @Override
    public Long getRecordsByOidAndUidAndStatus(String oid, String uid, String orderFilter) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().
                like("oid", oid).
                eq("uid", uid);
        if (orderFilter.equals("notPay")) {//待支付订单
            queryWrapper.eq("status", 0);
        } else if (orderFilter.equals("notReceive")) {//待收货订单
            queryWrapper.and(wrapper -> wrapper.eq("status", 1).or().eq("status", 2));
        } else if (orderFilter.equals("finish")) {//已完成
            queryWrapper.and(wrapper -> wrapper.eq("status", 3).or().eq("status", 6));
        } else if (orderFilter.equals("cancel")) {//已取消
            queryWrapper.and(wrapper -> wrapper.eq("status", 4).or().eq("status", 5));
        }
        return orderDao.selectCount(queryWrapper);
    }

}
