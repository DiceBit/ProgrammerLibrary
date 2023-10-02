package com.example.webapp3.Controller;

import com.example.webapp3.Models.Answer;
import com.example.webapp3.Models.Question;
import com.example.webapp3.Models.Test;
import com.example.webapp3.Repositories.QuestionRepository;
import com.example.webapp3.Repositories.TestRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    private String getUserTest(Model model){
        model.addAttribute("testList", testRepository.findAll());
        return "Testing";
    }


    @GetMapping("/addTest")
    private String addTestView(Model model){
        model.addAttribute("test", new Test());
        model.addAttribute("questions", Arrays.asList(new QuestionForm()));
        return "addTest";
    }

    @ResponseBody
    @PostMapping("/addTest")
    public String submitTest(@RequestParam Map<String, String> formData) {
        // Ваша логика обработки данных, например, сохранение в базу данных или другие операции

        // Выводим данные в консоль (в реальном приложении здесь может быть логика сохранения в базу данных, проверки и т.д.)
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        return "redirect:/tests";
    }

    /*@PostMapping("/addTest")
    private String addTest(@ModelAttribute Test test, @ModelAttribute List<QuestionForm> questions){

        //Test savedTest = testRepository.save(test);
        System.out.println("test: " + test);

        // Сохранение вопросов и ответов
        for (QuestionForm questionForm : questions) {
            Question question = new Question();
            question.setQuestionText(questionForm.getContent());

            //question.setTest(savedTest);

            List<Answer> answers = questionForm.getAnswers().stream()
                    .map(answerForm -> {
                        Answer answer = new Answer();
                        answer.setAnswerText(answerForm.getContent());
                        answer.setCorrect(answerForm.isCorrect());
                        answer.setQuestion(question);
                        return answer;
                    })
                    .collect(Collectors.toList());

            question.setAnswers(answers);

            //questionRepository.save(question);
        }




        System.out.println("Данные успешно забрались: "
                + "\nНазвание теста: " + test.getTestName()
                + "\ntest: " + test);

        //testRepository.save(test);
        return "redirect:/tests";
    }*/
}
















@Data
 class QuestionForm {
    private String content;
    private List<AnswerForm> answers;

}
@Data
 class AnswerForm {
    private String content;
    private boolean correct;

}