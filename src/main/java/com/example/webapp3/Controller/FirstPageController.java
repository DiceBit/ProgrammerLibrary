package com.example.webapp3.Controller;

import com.example.webapp3.Models.Book;
import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.BookRepository;
import com.example.webapp3.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FirstPageController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    private String getHome(Model model){
        return "home";
    }

    @GetMapping("/file/{bookFileName}")
    private String linkDownloadHandler(@PathVariable String bookFileName ){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Book infoBook = bookRepository.findByFileName(bookFileName);
        User user = userRepository.findByUsername(auth.getName());

        user.setBalance(user.getBalance() - infoBook.getPrice());
        userRepository.save(user);

        System.out.println("Ссылка нажата");
        System.out.println("bookFileName: " + bookFileName);
        System.out.println("UserName: " + auth.getName());
        System.out.println("isAuth: " + auth.isAuthenticated());
        System.out.println("\n==============\n");
        System.out.println("Info: " + user);
        System.out.println("Info: " + infoBook);


        return "main";
    }
}
