package com.example.bookclient;

import com.example.bookclient.controller.BookRestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BookClientApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(BookClientApplication.class, args);
		BookRestController bookRestController = context.getBean(BookRestController.class);
		//bookRestController.getBook_V1();
		//bookRestController.getBook_V2();
		bookRestController.getBook_V3();
	}

}
