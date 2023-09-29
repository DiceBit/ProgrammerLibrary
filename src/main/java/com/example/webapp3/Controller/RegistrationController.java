package com.example.webapp3.Controller;

import com.example.webapp3.Models.Role;
import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    private String getRegistration(){
        return "registration";
    }

    @PostMapping("/registration")
    private String addUser(User user, Model model){

        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null){
            model.addAttribute("userError", "user exist!");
            return "registration";
        }

        user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
        user.setBalance(10);
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);

        return "redirect:/login";
    }
}
