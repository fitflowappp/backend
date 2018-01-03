package com.magpie.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.didispace.swagger.EnableSwagger2Doc;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;


@SpringBootApplication
@EnableSwagger2Doc
@ComponentScan(basePackages = { "com.magpie" })
@EnableAsync
public class UserCenterApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserCenterApplication.class, args);
	}
	
	@Bean
	public Executor getAsyncExecutor() {// 实现AsyncConfigurer接口并重写getAsyncExecutor方法，并返回一个ThreadPoolTaskExecutor，这样我们就获得了一个基于线程池TaskExecutor
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(5);// 线程池维护线程的最少数量
		taskExecutor.setKeepAliveSeconds(30000);// 线程池维护线程所允许的空闲时间
		taskExecutor.setMaxPoolSize(200);// 线程池维护线程的最大数量
		taskExecutor.setQueueCapacity(25);// 线程池所使用的缓冲队列
		taskExecutor.initialize();
		return taskExecutor;
	}
}
