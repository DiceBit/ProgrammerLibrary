package com.example.webapp3.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    /*@GetMapping("/admin")
    private String userList(Model model){
        model.addAttribute("allUsers", userService.allUsers());
        return "admin";
    }

    @PostMapping("/admin")
    private String deleteUser(@RequestParam(required = true, defaultValue = "") Long userId,
                              @RequestParam(required = true, defaultValue = "") String action){
        if (action.equals("delete")){
            userService.deleteUser(userId);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/gt/{userId}")
    private String gtUser(@PathVariable("userId") Long userId, Model model){
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "admin";
    }*/
}
