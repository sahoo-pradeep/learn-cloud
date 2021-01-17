package com.example.bookservice.controller;

import com.example.bookservice.dao.BookDao;
import com.example.bookservice.model.Book;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BookController {

    private final BookDao bookDao;

    @Autowired
    public BookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @GetMapping("/book/v1/{id}")
    public Book getBook_v1(@PathVariable int id) {
        return Optional.ofNullable(bookDao.findById(id))
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @GetMapping("/book/v2/{id}")
    @HystrixCommand(fallbackMethod = "getFallBackBook")
    public Book getBook_v2(@PathVariable int id) {
        //With Hystrix Fallback
        return Optional.ofNullable(bookDao.findById(id))
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // When there is an exception, return this Object
    public Book getFallBackBook(int id) {
        System.out.println("Fallback method called with id: " + id);
        return Book.create("Sample Title", "Sample Author");
    }
}
