package com.example.gateway.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${book-service}")
public interface HttpBookService {

    @GetMapping("/book/v1/{id}")
    String getBook(@PathVariable int id);
}
