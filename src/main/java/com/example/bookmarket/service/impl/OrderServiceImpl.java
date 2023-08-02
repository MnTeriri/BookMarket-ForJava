package com.example.bookmarket.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookmarket.dao.IAddressDao;
import com.example.bookmarket.dao.IBookDao;
import com.example.bookmarket.dao.IOrderBookDao;
import com.example.bookmarket.dao.IOrderDao;
import com.example.bookmarket.job.OrderJob;
import com.example.bookmarket.model.Address;
import com.example.bookmarket.model.Book;
import com.example.bookmarket.model.Order;
import com.example.bookmarket.model.OrderBook;
import com.example.bookmarket.service.IOrderService;
import com.example.bookmarket.util.ImageUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IOrderBookDao orderBookDao;
    @Autowired
    private IBookDao bookDao;
    @Autowired
    private IAddressDao addressDao;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public Integer createOrder(Order order) {
        String oid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtil.randomNumbers(6);
        while (searchOrder(oid) != null) {//如果生成的订单号存在，则重新生成，直到不存在
            oid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtil.randomNumbers(6);
        }
        order.setOid(oid);
        Integer result = orderDao.createOrder(order);
        if (result == 1) {//如果订单创建成功
            // 设置订单定时任务，在15分钟后检查订单状态
            JobDetail jobDetail = JobBuilder.newJob(OrderJob.class)
                    .withIdentity(order.getOid(), "orderGroup")
                    .usingJobData("orderOid", order.getOid())
                    .build();
            LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(5);
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(order.getOid(), "orderGroup")
                    .startAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .build();
            try {
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                scheduler.scheduleJob(jobDetail, trigger);//添加订单定时任务
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
            scheduler.unscheduleJob(new TriggerKey(order.getOid(),"orderGroup"));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return orderDao.cancelOrder(order);
    }

    @Override
    public Integer updateOrderStatue(Order order) {
        return null;
    }

    @Override
    public Order searchOrder(String oid) {
        QueryWrapper<Order> queryWrapper=new QueryWrapper<Order>().eq("oid",oid);
        Order order = orderDao.selectOne(queryWrapper);
        addOrderOtherInf(order);
        return order;
    }

    @Override
    public List<Order> getOrderList(String oid, String uid, String orderFilter, Integer page, Integer count) {
        Page<Order> p = new Page<>(page, count);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().
                like("oid", oid).
                eq("uid", uid).
                orderByDesc("created_time");
        List<Order> list = new ArrayList<>();
        if (orderFilter.equals("notPay")) {//待支付订单
            queryWrapper.eq("status", 0);
        } else if (orderFilter.equals("notReceive")) {//待收货订单
            queryWrapper.and(wrapper -> wrapper.eq("status", 1).or().eq("status", 2));
        } else if (orderFilter.equals("finish")) {//已完成
            queryWrapper.and(wrapper -> wrapper.eq("status", 3).or().eq("status", 6));
        } else if (orderFilter.equals("cancel")) {//已取消
            queryWrapper.and(wrapper -> wrapper.eq("status", 4).or().eq("status", 5));
        }
        list = orderDao.selectPage(p, queryWrapper).getRecords();
        for (Order order : list) {
            addOrderOtherInf(order);
        }
        return list;
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

    public void addOrderOtherInf(Order order){
        Address address = addressDao.selectOne(new QueryWrapper<Address>().eq("id", order.getAid()));
        List<OrderBook> books = orderBookDao.selectList(new QueryWrapper<OrderBook>().eq("oid", order.getOid()));
        for (OrderBook orderBook : books) {
            Book book = bookDao.selectOne(new QueryWrapper<Book>().eq("bid", orderBook.getBid()));
            byte[] image = book.getImage();
            String imageString = "";
            if (image != null) {
                imageString = ImageUtils.encodeImageString(image);
            }
            book.setImageString(imageString);
            book.setImage(null);
            orderBook.setBook(book);
        }
        order.setAddress(address);
        order.setBooks(books);
    }
}
