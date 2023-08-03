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
所以采用Quartz，因为Quartz定时器在创建时有一个Trigger，当用户在15分钟内付款，我可以根据TriggerKey来关闭对应的定时器，这样效率会高很多

### 2023/8/2
* 相关类的介绍：  
* 1. SchedulerFactoryBean是Spring Framework中提供的一个用于配置和创建Quartz Scheduler的工厂类。它允许将 Quartz Scheduler集成到Spring应用程序中，并通过 Spring 的依赖注入来管理Quartz的配置和调度任务
* 2. JobDetail是Quartz框架中的一个类，用于定义和描述一个具体的Job（任务）实例。它包含了执行Job的相关信息，比如Job的类名、Job的名称、Job的组名、Job执行时所需的数据等
* 3. Trigger是用于触发Job（任务）执行的对象，它定义了Job 的执行时间和调度规则。Trigger控制着Job在何时执行以及如何重复执行

* 创建了QuartzConfig类，用于配置Quartz，其中有一个schedulerFactoryBean()方法，返回一个SchedulerFactoryBean，给相应的Service层调用
* 创建了OrderJob类，用于实现定时器到时间后所执行的任务，需继承Job接口，实现execute方法。该方法有一个参数，为JobExecutionContext对象，该对象能获取创建定时任务时的JobDetail对象和Trigger对象，继而获取在JobDetail对象添加的数据。

* 在OrderService创建了createOrder()，用于创建订单。首先生成20位订单号（当前时间日期+6位随机数），然后查询该订单号是否存在，如果存在则重新生成。
  然后调用OrderDao的createOrder()创建订单（还未实现），当创建成功后，给订单添加定时任务。
  添加定时任务中使用了JobBuilder类、TriggerBuilder类创建JobDetail对象和Trigger对象 并使用withIdentity()方法设置JobDetail对象和Trigger对象的名称和组名，
  使用usingJobData()给JobDetail对象添加数据，最后调用SchedulerFactoryBean对象的getScheduler()获取Scheduler对象，
  使用Scheduler的scheduleJob()添加定时任务

* 在OrderService创建了cancelOrder()，用于取消订单。首先调用SchedulerFactoryBean对象的getScheduler()获取Scheduler对象，
  使用Scheduler的unscheduleJob()删除需要取消订单的定时任务，取消定时任务后调用OrderDao的cancelOrder()取消订单（还未实现）
* 在OrderService创建了searchOrder()，使用订单号搜索订单。使用了QueryWrapper

* 实现了OrderJob类的execute()，使用JobExecutionContext对象的getJobDetail()获取JobDetail对象，再使用getJobDataMap()获取JobDataMap对象
  该对象就是创建定时任务时为JobDetail对象添加的数据，在这里我给他添加了订单号，获取后调用OrderService对象的searchOrder()搜索最新的订单状态，
  当订单还未付款，则调用OrderDao的cancelOrder()取消订单（还未实现），并打印相应消息

### 2023/8/3
* 添加了AddressService，用于实现地址信息的操作，目前实现了searchDefaultAddress()，用于搜索用户的默认地址
* 实现了购物车全选功能，给CartController、CartService、CartDAO添加了selectAllCart()方法（由于UpdateWrapper没法连接查询，这里用@Update()注解编写自定义sql）
* CartController的getCartList()进行功能添加，现在把用户默认地址信息进行查询并返回给表示层
