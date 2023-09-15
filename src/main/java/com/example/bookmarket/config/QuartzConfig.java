package com.example.bookmarket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Slf4j
@Configuration
public class QuartzConfig {
    public QuartzConfig() {
        log.debug("创建配置类对象：QuartzConfig");
    }

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
        jobFactory.setApplicationContext(SpringContextUtils.context);
        schedulerFactoryBean.setJobFactory(jobFactory);

        return schedulerFactoryBean;
    }
}
