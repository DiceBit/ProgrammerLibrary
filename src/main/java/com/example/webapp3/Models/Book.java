package com.example.webapp3.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookName;
    private String tag;
    private int price;
    private String fileName;
    private Date date = new Date();


    public Book(String bookName, String tag, int price) {
        this.bookName = bookName;
        this.tag = tag;
        this.price = price;
    }

}
