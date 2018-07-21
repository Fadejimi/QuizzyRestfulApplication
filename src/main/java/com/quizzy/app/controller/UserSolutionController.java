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
import com.quizzy.app.model.UserSolution;
import com.quizzy.app.repository.QuizRepository;
import com.quizzy.app.repository.UserRepository;
import com.quizzy.app.repository.UserSolutionRepository;
import com.quizzy.app.utils.RestUtility;

@RestController
@RequestMapping("/api")
public class UserSolutionController {
	@Autowired
	UserSolutionRepository userSolutionRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	QuizRepository quizRepository;
	
	@GetMapping("/users/{id}/solutions")
	public List<UserSolution> getUserSolutionsByUser(@PathVariable("id") long id) {
		return userSolutionRepository.findUserSolutionsByUserId(id);
	}
	
	@GetMapping("/quizzes/{id}/solutions")
	public List<UserSolution> getUserSolutionsByQuiz(@PathVariable("id") long id) {
		return userSolutionRepository.findUserSolutionsByQuizId(id);
	}
	
	@PostMapping("/quizzes/{id}/solutions")
	public ResponseEntity<Object> createUserSolutions(@PathVariable("id") long id,
			@Valid @RequestBody UserSolution userSolution) {
		quizRepository.findById(id).map(quiz -> {
			userSolution.setQuiz(quiz);
			userSolution.setUser(quiz.getCreator());
			
			UserSolution u = userSolutionRepository.save(userSolution);
			return RestUtility.getResponseEntity(u.getId());
		}).orElseThrow( () -> new ResourceNotFoundException("quiz", "id", id));
	}
	
	@PutMapping("/quizzes/{quiz_id}/solutions/{id}")
	public ResponseEntity<UserSolution> updateUserSolution(@PathVariable("quiz_id") long quiz_id,
			@PathVariable("id") long id,
			@Valid @RequestBody UserSolution requestUserSolution) {
		if (!quizRepository.existsById(quiz_id)) {
			throw new ResourceNotFoundException("quiz", "quiz_id", quiz_id);
		}
		
		userSolutionRepository.findById(id).map(userSolution -> {
			userSolution.setCorrect_answers(requestUserSolution.getCorrect_answers());
			userSolution.setTotal_questions(requestUserSolution.getTotal_questions());
			
			UserSolution u = userSolutionRepository.save(userSolution);
			return new ResponseEntity<UserSolution>(u, HttpStatus.OK);
		}).orElseThrow( () -> new ResourceNotFoundException("user solution", "id", id));
	}
	
	@GetMapping("/quizzes/{quiz_id}/solutions/{id}")
	public UserSolution getUserSolution(@PathVariable("quiz_id") long quiz_id, 
			@PathVariable("id") long id, 
			@Valid @RequestBody UserSolution requestUserSolution) {
		if (!quizRepository.existsById(quiz_id)) {
			throw new ResourceNotFoundException("quiz", "quiz_id", quiz_id);
		}
		
		return userSolutionRepository.findById(id).orElseThrow(() -> 
					new ResourceNotFoundException("user solution", "id", id));
	}
	
	@DeleteMapping("/quizzes/{quiz_id}/solutions/{id}")
	public ResponseEntity<UserSolution> deleteUserSolution(@PathVariable("quiz_id") long quiz_id,
			@PathVariable("id") long id) {
		if (!quizRepository.existsById(quiz_id)) {
			throw new ResourceNotFoundException("quiz", "quiz_id",  quiz_id);
		}
		
		return userSolutionRepository.findById(id).map(userSolution -> {
			userSolutionRepository.delete(userSolution);
			
			return new ResponseEntity<UserSolution>(HttpStatus.NO_CONTENT);
		}).orElseThrow(() -> new ResourceNotFoundException("user solution", "id", id));
	}
}
