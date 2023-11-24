package com.example.webapp3.Repositories;

import com.example.webapp3.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTag(String tag);
    Book findByFileName(String fileName);

}
