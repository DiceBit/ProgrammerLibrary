package com.example.webapp3.Controller;

import com.example.webapp3.Repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/{testId}")
    private String getQuestion(@PathVariable Long testId,
                               Model model){
        model.addAttribute(questionRepository.findByTestId(testId));
        return "questions";
    }
}
