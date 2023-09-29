package com.example.webapp3.Controller;

import com.example.webapp3.Models.Book;
import com.example.webapp3.Repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/main")
public class MainController {

    @Autowired
    private BookRepository bookRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    private String viewAndAddBook(Model model) {

        model.addAttribute("bookList", bookRepository.findAll());

        return "Test";
    }

    //@PostMapping
    @RequestMapping(value = "", method = RequestMethod.POST)
    private String addBook(@RequestParam String bookName,
                           @RequestParam String bookTag,
                           @RequestParam int bookPrice,
                           Model model) {

        Book book = new Book(bookName, bookTag, bookPrice);
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
        return "Test";
    }

    /*@GetMapping("/img")
    private String saveImgAddAttribute(Model model){
        model.addAttribute("filename", fileRepository.findAll());
        return "userList";
    }

    @PostMapping("/img")
    private String saveImg( @RequestParam("file") MultipartFile file) throws IOException {

        if (file != null){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(resultFileName));

            Book uploadFile = new Book(resultFileName);

            fileRepository.save(uploadFile);
        }

        return "redirect:/user";
    }*/

}
