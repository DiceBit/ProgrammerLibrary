package com.example.webapp3.Controller;

import com.example.webapp3.Models.Book;
import com.example.webapp3.Repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@EnableWebMvc
@RequestMapping("/main")
public class MainController {

    @Autowired
    private BookRepository bookRepository;

    @Value("${upload.path}")
    private String uploadPath;
    @Value("${upload.ImgPath}")
    private String uploadImgPath;

    @GetMapping
    private String viewAndAddBook(Model model) {

        model.addAttribute("bookList", bookRepository.findAll());

        return "Main";
    }

    //@PostMapping
    @RequestMapping(value = "", method = RequestMethod.POST)
    private String addBook(@RequestParam String bookName,
                           @RequestParam String bookTag,
                           @RequestParam int bookPrice,
                           @RequestParam("bookFile") MultipartFile bookFile,
                           @RequestParam("bookImg") MultipartFile bookImg,
                           Model model) throws IOException {

        Book book = new Book(bookName, bookTag, bookPrice);

        if (bookFile != null){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFile = uuidFile + "." + bookFile.getOriginalFilename();
            bookFile.transferTo(new File(uploadPath + "/" + resultFile));
            book.setFileName(resultFile);
        }
        if (bookImg != null){
            File uploadDir = new File(uploadImgPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFile = uuidFile + "." + bookImg.getOriginalFilename();
            bookImg.transferTo(new File(uploadImgPath + "/" + resultFile));
            book.setImg(resultFile);
        }

        bookRepository.save(book);

        model.addAttribute("bookList", bookRepository.findAll());

        return "redirect:/main";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = "bookFilter")
    private String filter(@RequestParam String bookFilter,
                          Model model) {

        Iterable<Book> bookList;

        if (bookFilter != null && !bookFilter.isEmpty()) {
            bookList = bookRepository.findByTag(bookFilter);
        } else {
            bookList = bookRepository.findAll();
        }
        model.addAttribute("bookList", bookList);
        model.addAttribute("bookFilter", bookFilter);

        return "Main";
    }


    @PostMapping("/button")
    private String getButton(@RequestParam String buttonValue,
                             Model model){

        System.out.println("Нажата кнопка: " + buttonValue);
        model.addAttribute("buttonValue", buttonValue);

        return "redirect:/main";
    }

}
