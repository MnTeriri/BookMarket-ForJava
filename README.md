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
* 代码如下：

~~~Java
@Configuration
public class QuartzConfig {
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        // 设置触发器是否覆盖已存在的任务
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        // 设置调度器是否自动启动
        schedulerFactoryBean.setAutoStartup(true);
        // 设置调度器的名称为OrderScheduler
        schedulerFactoryBean.setSchedulerName("OrderScheduler");
        // 设置调度器的启动延迟为 1 秒
        schedulerFactoryBean.setStartupDelay(1);
        // 设置调度器的应用程序上下文调度器上下文键为 "applicationContext"
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        return schedulerFactoryBean;
    }

}

@Component
public class OrderJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //对应的定时任务处理模块
    }
}
~~~

* 在OrderService创建了createOrder()，用于创建订单。首先生成20位订单号（当前时间日期+6位随机数），然后查询该订单号是否存在，如果存在则重新生成。
  然后调用OrderDao的createOrder()创建订单（还未实现），当创建成功后，给订单添加定时任务。
  添加定时任务中使用了JobBuilder类、TriggerBuilder类创建JobDetail对象和Trigger对象 并使用withIdentity()
  方法设置JobDetail对象和Trigger对象的名称和组名，
  使用usingJobData()给JobDetail对象添加数据，最后调用SchedulerFactoryBean对象的getScheduler()获取Scheduler对象，
  使用Scheduler的scheduleJob()添加定时任务
* 代码如下：
~~~Java
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    
    @Override
    public Integer createOrder(Order order) {
        //订单创建处理...
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
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
~~~

* 在OrderService创建了cancelOrder()，用于取消订单。首先调用SchedulerFactoryBean对象的getScheduler()获取Scheduler对象，
  使用Scheduler的unscheduleJob()删除需要取消订单的定时任务，取消定时任务后调用OrderDao的cancelOrder()取消订单（还未实现）
* 在OrderService创建了searchOrder()，使用订单号搜索订单。使用了QueryWrapper
* 代码如下：
~~~Java
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public Integer cancelOrder(Order order) {
        try {//删除对应订单的定时任务
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.unscheduleJob(new TriggerKey(order.getOid(), "orderGroup"));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        //订单取消处理...
        return (Integer) data.get("result");
    }
}
~~~

* 实现了OrderJob类的execute()，使用JobExecutionContext对象的getJobDetail()获取JobDetail对象，再使用getJobDataMap()
  获取JobDataMap对象
  该对象就是创建定时任务时为JobDetail对象添加的数据，在这里我给他添加了订单号，获取后调用OrderService对象的searchOrder()
  搜索最新的订单状态，
  当订单还未付款，则调用OrderDao的cancelOrder()取消订单（还未实现），并打印相应消息

### 2023/8/3

* 添加了AddressService，用于实现地址信息的操作，目前实现了searchDefaultAddress()，用于搜索用户的默认地址
* 实现了购物车全选功能，给CartController、CartService、CartDAO添加了selectAllCart()
  方法（由于UpdateWrapper没法连接查询，这里用@Update()注解编写自定义sql）
* CartController的getCartList()进行功能添加，现在把用户默认地址信息进行查询并返回给表示层

### 2023/8/12

* 上述未实现功能全部实现。需要注意的是，在OrderJob类当中如需使用@Autowired，需进行如下配置：
* 其实就是让SpringBoot掌管OrderJob对象的创建
~~~Java
@Component
public class SpringContextUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }
}

public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
    private transient AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }
}

@Configuration
public class QuartzConfig {
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        // 设置触发器是否覆盖已存在的任务
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        // 设置调度器是否自动启动
        schedulerFactoryBean.setAutoStartup(true);
        // 设置调度器的名称为OrderScheduler
        schedulerFactoryBean.setSchedulerName("OrderScheduler");
        // 设置调度器的启动延迟为 1 秒
        schedulerFactoryBean.setStartupDelay(1);
        // 设置调度器的应用程序上下文调度器上下文键为 "applicationContext"
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(SpringContextUtils.applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);

        return schedulerFactoryBean;
    }
}
~~~
* 更改了getListOrder()的查询方法，现在使用的是Mybatis的分步查询，代码如下：
~~~Java
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
}

//用于组装复杂sql
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

@Repository
public interface IAddressDao extends BaseMapper<Address> {
    //分步查询会自动执行该方法
    @Select("SELECT * FROM Address WHERE id=#{aid}")
    public Address searchById(Integer aid);
}

@Repository
public interface IOrderBookDao extends BaseMapper<OrderBook> {
    //分步查询会自动执行该方法
    @Select("SELECT * FROM Order_Book WHERE oid=#{oid}")
    @Results({
            @Result(property = "bid", column = "bid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "book", column = "bid", one = @One(select = "com.example.bookmarket.dao.IBookDao.searchBookByBid"))
    })
    public List<OrderBook> getOrderDetail(String oid);
}

@Repository
public interface IBookDao extends BaseMapper<Book> {
    //分步查询会自动执行该方法
    @Select("SELECT * FROM Book WHERE bid=#{bid}")
    @Results({
            @Result(property = "imageString", column = "image", typeHandler = ImageUtils.class)
    })
    public Book searchBookByBid(String bid);
}

//对照片进行base64加密的工具类
public class ImageUtils extends BaseTypeHandler<String> {
    public static String encodeImageString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static InputStream decodeImageString(String imageString) {
        byte[] bytes = Base64.getDecoder().decode(imageString);
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        throw new UnsupportedOperationException("This type handler is read-only.");
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        byte[] bytes = rs.getBytes(columnName);
        return bytes != null ? encodeImageString(bytes) : null;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        byte[] bytes = rs.getBytes(columnIndex);
        return bytes != null ? encodeImageString(bytes) : null;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        byte[] bytes = cs.getBytes(columnIndex);
        return bytes != null ? encodeImageString(bytes) : null;
    }
}
~~~