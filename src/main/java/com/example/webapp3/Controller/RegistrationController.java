package com.example.webapp3.Controller;

import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.UserRepository;
import com.example.webapp3.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    private String getRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    private String addUser(User user, Model model) {

       /* User userFromDb = userRepository.findByUsername(user.getUsername());
        User userEmailFromDb = userRepository.findByEmail(user.getEmail());

        if (userEmailFromDb != null) {
            model.addAttribute("userError", "email exist!");
            return "registration";
        }

        if (userFromDb != null) {
            model.addAttribute("userError", "user exist!");
            return "registration";
        }
        if (user.getPassword().length() <= 2) {
            model.addAttribute("passwordError", "Password less 2");
            return "registration";
        }

        user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
        user.setEmail(user.getEmail());
        user.setActivationCode(UUID.randomUUID().toString());
        user.setBalance(10);
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        if (!StringUtils.isEmpty(user.getEmail())){

        }

        StringBuilder activityText = new StringBuilder("user registration");
        activityText.append(" on ")
                .append(LocalDateTime.now()
                        .format(DateTimeFormatter
                                .ofPattern("dd-MM-yyyy HH:mm")));


        user.setActivity(Arrays.asList(new Active(activityText)));
        userRepository.save(user);*/

        if (!userService.addUser(user)){
            model.addAttribute("userError", "user exist!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    private String activationAcc(Model model ,@PathVariable String code){

        boolean isActivate  = userService.activateUser(code);

        if (isActivate){
            model.addAttribute("message", "Activation successful");
        } else {
            model.addAttribute("message", "Activation code isn't found");
        }

        return "login";
    }
}
