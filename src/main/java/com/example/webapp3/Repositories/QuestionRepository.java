package com.example.webapp3.Repositories;

import com.example.webapp3.Models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository  extends JpaRepository<Question, Long> {

    List<Question> findByTestId(Long testId);

}
