package com.example.webapp3.Repositories;

import com.example.webapp3.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTag(String tag);
}
