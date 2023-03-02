package com.dh.revisao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DhContaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DhContaApplication.class, args);
	}

}
