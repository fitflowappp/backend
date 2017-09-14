package com.magpie.yoga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.didispace.swagger.EnableSwagger2Doc;

@SpringBootApplication
@EnableSwagger2Doc
@ComponentScan(basePackages = { "com.magpie" })
public class YogaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YogaApplication.class, args);
	}
}
