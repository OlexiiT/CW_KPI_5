package com.example.demo4.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookName;
    private Long userId;
    private String text;

    public Book() {
    }

    public Long getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }
}
