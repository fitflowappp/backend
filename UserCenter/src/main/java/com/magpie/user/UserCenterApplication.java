package com.magpie.uc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.magpie"})  
public class UserCenterApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(UserCenterApplication.class, args);
	}
}
