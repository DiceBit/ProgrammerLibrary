package com.example.webapp3.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO: 30.10.2023 сделать описание книги, отзывы  
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
    private String Img;
    private String fileName;
    private Date date = new Date();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    @Column(columnDefinition = "varchar(255) default 'No reviews yet'")
    private List<Reviews> reviews = new ArrayList<>();

    public Object originalBookFileName() {

        int index = fileName.indexOf(".");
        if (index != -1) {
            String originalName = fileName.substring(index + 1);
            if (!originalName.isEmpty()) {
                return fileName;
            } else {
                return null;
            }
        }
        return null;
    }

    public Book(String bookName, String tag, int price) {
        this.bookName = bookName;
        this.tag = tag;
        this.price = price;
    }

}
