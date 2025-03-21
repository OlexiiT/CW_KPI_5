package com.example.demo4.controller;

import com.example.demo4.model.Book;
import com.example.demo4.model.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepo bookRepo;

    public void saveBook(String name, String text, Long userId) {
        Book book = new Book(name, text, userId);
        addBook(book);
    }

    public void addBook(Book book) {
        bookRepo.save(book);
    }

    public List<Book> getOwnerBooksList(Long userId) {
        ArrayList <Book> books = new ArrayList<>();
        books = bookRepo.findAllByUserId(userId);
        return books;
    }

    public Book getBookById(Long bookId) {
        Optional<Book> optionalBook = bookRepo.findById(bookId);
        if (optionalBook.isEmpty()) return null;
        return optionalBook.get();
    }

    public List<Book> getRecomendedBooks() {
        List<Book> allBooks = bookRepo.findAll();
        int toIndex = 0;
        if (!allBooks.isEmpty()) {
            if (allBooks.size() > 100) toIndex = 100;
            else toIndex = allBooks.size();
        }
        return allBooks.subList(0, toIndex);
    }

    public List<Book> getSearchResults(String query) {
        return bookRepo.findAllByBookName(query);
    }
}
