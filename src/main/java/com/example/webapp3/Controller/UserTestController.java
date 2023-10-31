package com.example.webapp3.Controller;

import com.example.webapp3.Models.Question;
import com.example.webapp3.Models.Test;
import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.TestRepository;
import com.example.webapp3.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/test")
public class UserTestController {
    private static int score;

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    private String getTests(Model model) {
        model.addAttribute("testList", testRepository.findAll());
        return "Testing";
    }

    @GetMapping("/addTest")
    private String getAddTest() {
        return "addTest";
    }

    @GetMapping("/{id}")
    private String getTest(@PathVariable Long id,
                           Model model) {

        model.addAttribute("getTest", testRepository.findById(id).get());
        return "testPage";
    }

    @PostMapping("/sendResult")
    public String getUserAnswer(@RequestParam Map<String, String> allParams) {

        System.out.println(allParams);

        List<String> questionList = new ArrayList<>();
        List<String> correctAnswersList = new ArrayList<>();
        List<String> userAnswersList = new ArrayList<>();


        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().startsWith("questions"))
                questionList.add(entry.getValue());
            else if (entry.getKey().startsWith("correctAnswers"))
                correctAnswersList.add(entry.getValue());
            else if (entry.getKey().startsWith("userAnswers"))
                userAnswersList.add(entry.getValue());
        }

        score = 0;

        for (String correctAnswer : correctAnswersList) {
            for (String userAnswer : userAnswersList) {
                if (userAnswer.equals(correctAnswer)) {
                    score++;
                }
            }
        }

        System.out.println("userScore: " + score);

        if (false) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUsername(auth.getName());

            user.setBalance(user.getBalance() + score);

            System.out.println("Data completely save!"  );
        }

        return "redirect:/test/confirm";
    }

    @GetMapping("/confirm")
    public String confirmTest(Model model) {
        model.addAttribute("message", "Score: " + score);
        return "testSendSubmit";
    }

}
