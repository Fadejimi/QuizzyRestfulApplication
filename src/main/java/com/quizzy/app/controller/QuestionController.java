package com.quizzy.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizzy.app.exception.ResourceNotFoundException;
import com.quizzy.app.model.Question;
import com.quizzy.app.model.Quiz;
import com.quizzy.app.repository.QuestionRepository;
import com.quizzy.app.repository.QuizRepository;
import com.quizzy.app.utils.RestUtility;

@RestController
@RequestMapping("/api")
public class QuestionController {
	@Autowired
	QuestionRepository questionRepository;
	
	@Autowired
	QuizRepository quizRepository;
	
	@GetMapping("/quizzes/{id}/questions")
	public List<Question> getQuestions(@PathVariable("id") long id) {
		return questionRepository.findQuestionsByQuizId(id);
	}
	
	@PostMapping("/quizzes/{id}/questions")
	public ResponseEntity<Object> createQuestions(@PathVariable("id") long id, 
			@Valid @RequestBody Question question) {
		quizRepository.findById(id).map(quiz -> {
			question.setQuiz(quiz);
			
			Question questionResponse = questionRepository.save(question);
			return RestUtility.getResponseEntity(questionResponse.getId());
		}).orElseThrow(() -> new ResourceNotFoundException("quiz", "id", id));
	}
	
	@PutMapping("/quizzes/{quiz_id}/questions/{id}")
	public ResponseEntity<Question> updateQuestion(@PathVariable("quiz_id") long quiz_id,
			@PathVariable("id") long id,
			@Valid @RequestBody Question questionRequest) {
		if (!quizRepository.existsById(quiz_id)) {
			throw new ResourceNotFoundException("quiz", "id", quiz_id);
		}
		
		questionRepository.findById(id).map(question -> {
			question.setQuestion(questionRequest.getQuestion());
			Question q = questionRepository.save(question);
			
			return new ResponseEntity<Question>(HttpStatus.NO_CONTENT);
		}).orElseThrow(() -> new ResourceNotFoundException("question", "id", id));
	}
	
	@GetMapping("/quizzes/{quiz_id}/questions/{id}")
	public Question getQuestion(@PathVariable("quiz_id") long quiz_id, 
			@PathVariable("id") long id) {
		if (!quizRepository.existsById(quiz_id)) {
			throw new ResourceNotFoundException("quiz", "id", quiz_id);
		}
		
		return questionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("question", "id", id));
	}
	
	@DeleteMapping("/quizzes/{quiz_id}/questions/{id}")
	public ResponseEntity<Question> deleteQuestion(@PathVariable("quiz_id") long quiz_id,
			@PathVariable("id") long id) {
		if (!quizRepository.existsById(quiz_id)) {
			throw new ResourceNotFoundException("quiz", "id", quiz_id);
		}
		
		questionRepository.findById(id).map(question -> {
			questionRepository.delete(question);
			return new ResponseEntity<Question>(HttpStatus.NO_CONTENT);
		}).orElseThrow(() -> new ResourceNotFoundException("question", "id", id));
	}
}
