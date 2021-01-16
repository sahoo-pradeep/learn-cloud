package com.example.bookservice.model;

public class Book {
    private int id;
    private String title;
    private String author;

    private Book(){

    }

    private Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public Book cloneWithId(int id) {
        return new Book(id, this.title, this.author);
    }

    public static Book create(String title, String author) {
        Book book = new Book();
        book.title = title;
        book.author = author;

        return book;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
