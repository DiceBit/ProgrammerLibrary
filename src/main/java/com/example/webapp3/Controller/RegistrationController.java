package com.example.webapp3.Controller;

import com.example.webapp3.Models.Active;
import com.example.webapp3.Models.Role;
import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    private String getRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    private String addUser(User user, Model model) {

        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.addAttribute("userError", "user exist!");
            return "registration";
        }
        if (user.getPassword().length() <= 2) {
            model.addAttribute("passwordError", "Password less 2");
            return "registration";
        }

        user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
        user.setBalance(10);
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        StringBuilder activityText = new StringBuilder("user registration");
        activityText.append(" on ")
                .append(LocalDateTime.now()
                        .format(DateTimeFormatter
                                .ofPattern("dd-MM-yyyy HH:mm")));


        user.setActivity(Arrays.asList(new Active(activityText)));
        userRepository.save(user);

        return "redirect:/login";
    }
}
