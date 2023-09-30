package com.example.webapp3.Controller;

import com.example.webapp3.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstPageController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    private String getHome(Model model){
        return "home";
    }
}
