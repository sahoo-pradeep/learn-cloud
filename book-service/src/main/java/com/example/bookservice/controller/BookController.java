package com.example.bookservice.controller;

import com.example.bookservice.dao.BookDao;
import com.example.bookservice.model.Book;
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
        System.out.println("getBook_v1() with id: " + id);
        return Optional.ofNullable(bookDao.findById(id))
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }
}
