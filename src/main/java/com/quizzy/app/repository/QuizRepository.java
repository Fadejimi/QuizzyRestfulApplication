package com.quizzy.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizzy.app.model.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long>{
	public List<Quiz> findQuizzesByCourseId(long id);
	public List<Quiz> findQuizzesByCreatorId(long id);
}
