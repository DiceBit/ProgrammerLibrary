package com.example.webapp3.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/test")
    public String getTest(){
        return "Test";
    }



}
