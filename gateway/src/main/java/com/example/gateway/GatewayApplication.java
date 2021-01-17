package com.example.gateway;

import com.example.gateway.controller.BookRestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(GatewayApplication.class, args);
		BookRestController bookRestController = context.getBean(BookRestController.class);
		//bookRestController.getBook_V1();
		//bookRestController.getBook_V2();
		bookRestController.getBook_V3();
	}

}
