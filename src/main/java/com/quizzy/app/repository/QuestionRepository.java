package com.quizzy.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizzy.app.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{
	public List<Question> findQuestionsByQuizId(long id);
}
