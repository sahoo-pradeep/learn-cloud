package com.example.bookservice.dao;


import com.example.bookservice.model.Book;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookDao {
    private final Map<Integer, Book> bookStore = new HashMap<>();
    private int currentId = 1;

    public List<Book> findAll() {
        return new ArrayList<>(bookStore.values());
    }

    public Book findById(int id) {
        return bookStore.get(id);
    }

    public Book save(Book book) {
        Book newBook = book.cloneWithId(currentId++);
        bookStore.put(newBook.getId(), newBook);
        return newBook;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Book godan = Book.create( "Godan", "Prem Chand");
        Book romeoJuliet = Book.create("Romeo Juliet", "Shakespeare");
        save(godan);
        save(romeoJuliet);

        System.out.println("BookDao initialization completed.");
        System.out.println("Added " + bookStore.size() + " books");
    }
}
