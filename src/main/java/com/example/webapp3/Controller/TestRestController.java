package com.example.webapp3.Controller;

import com.example.webapp3.Models.Question;
import com.example.webapp3.Models.Test;
import com.example.webapp3.Repositories.TestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestRestController {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/test/submit-test")
    public Iterable<Test> getSubmitTest() {
        return testRepository.findAll();
    }

    @PostMapping("/test/submit-test")
    public ResponseEntity<String> createTest(@RequestBody String json) {
        try {
            Test test = objectMapper.readValue(json, Test.class);
            processTest(test);
            testRepository.save(test);
            return new ResponseEntity<>("Test created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating test: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void processTest(Test test) {
        test.getQuestions().forEach(this::processQuestion);
    }

    //delete bottom function
    private void processQuestion(Question question) {
        question.getAnswers().forEach(answer -> {
            if (answer.getAnswerText().equals(question.getCorrectAnswerText())) {
            }
        });
    }




}
