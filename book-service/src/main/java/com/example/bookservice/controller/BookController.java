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

    private BookDao bookDao;

    @Autowired
    public BookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @GetMapping("/book/{id}")
    public Book getBook(@PathVariable int id) {
        return Optional.of(bookDao.findById(id))
                .orElseThrow();
    }
}
