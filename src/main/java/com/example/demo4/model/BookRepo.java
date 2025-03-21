package com.example.demo4.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    ArrayList<Book> findAllByUserId(Long userId);

    List<Book> findAllByBookName(String query);
}
