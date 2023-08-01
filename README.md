# 网上书店（BookMark）

## 项目说明

这是一个Java项目，采用前后端分离，实现网上书店的基本功能，比如：

* 用户登录注册
* 图书商品的查询和浏览
* 购物车系统
* 订单系统（可退货可取消）
* 用户基本信息管理
* ...（没想好，后面再改）

## 使用的框架

* SpringBoot
* Mybatis-Plus
* ...（没想好，后面再改）

## 项目进展

### 2023/7/21
* 上传到了GitHub
* 截止到7月21号，实现了如下功能：
* 1. 登录和注册请求的处理
* 2. 商品页面的分页、查询的处理
* 3. 实体类的建立
* 4. 基本DAO的建立

### 2023/7/22
实现了如下功能：
* 编写了OrderController的orderList()，处理前端查询请求（订单号、分页数据还未处理）
* 编写了OrderService的getOrderList()，处理查询订单信息的请求（目前只能处理查询所有订单，分页和订单号还是给死）
* 对订单实体类（Order）进行了修改，之前在@TableName()注解的表名有误，导致QueryWrapper执行出问题（保留字需加上``）
* 对订单查询类型起了名（”all“：所有订单，”notPay“：待支付，”notReceive“：待收货，”finish“：已完成，”cancel“：已取消）

### 2023/7/31
* OrderService的getOrderList()所有功能全部完成（订单号搜索、订单类型搜索、分页）
* 给OrderService添加了getRecordsByOidAndUidAndStatus()，用于计算经过筛选后的订单数量，以全部完成
* OrderController的orderList()所有功能全部完成，并在这里计算页数返回到前端

### 2023/8/1
* 看了一下定时任务的实现，有两种，一种是SpringBoot的ThreadPoolTaskScheduler，另一种是Quartz。
在这里我选择了Quartz，由于我想实现当订单付款时取消订单任务，
如果使用ThreadPoolTaskScheduler， 需要一个Map来存放对应订单id和ScheduledFuture对象，当订单量超过一定数量，这个Map必爆炸
所以采用Quartz，因为Quartz定时器在创建时有一个Trigger，当用户在15分钟内付款，我可以根据TriggerKey来关闭对应的定时器，这样效率会高很多。
