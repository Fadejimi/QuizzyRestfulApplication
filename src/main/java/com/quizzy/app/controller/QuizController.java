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
import com.quizzy.app.model.Quiz;
import com.quizzy.app.repository.CourseRepository;
import com.quizzy.app.repository.QuizRepository;
import com.quizzy.app.utils.RestUtility;

@RestController
@RequestMapping("/api")
public class QuizController {
	
	@Autowired
	QuizRepository quizRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@GetMapping("/courses/{id}/quizzes")
	public List<Quiz> getQuizzesByCourse(@PathVariable("id") long id) {
		return quizRepository.findQuizzesByCourseId(id);
	}
	
	@PostMapping("/courses/{id}/quizzes")
	public ResponseEntity<Object> createQuiz(@PathVariable("id") long id, 
			@Valid @RequestBody Quiz quiz) {
		courseRepository.findById(id).map(course -> {
			quiz.setCourse(course);
			Quiz quizReponse = quizRepository.save(quiz);
			return RestUtility.getResponseEntity(quizReponse.getId());
		}).orElseThrow(() -> new ResourceNotFoundException("course", "id", id));
	}
	
	@PutMapping("/courses/{course_id}/quizzes/{id}")
	public ResponseEntity<Quiz> updateQuiz(@PathVariable("course_id") long course_id, 
			@PathVariable("id") long id, 
			@Valid @RequestBody Quiz quiz) {
		if (!courseRepository.existsById(course_id)) {
			throw new ResourceNotFoundException("course", "id", course_id);
		}
		
		quizRepository.findById(id).map(quizResponse -> {
			quizResponse.setName(quiz.getName());
			Quiz q = quizRepository.save(quizResponse);
			
			return new ResponseEntity<Quiz>(q, HttpStatus.OK);
		}).orElseThrow(() -> new ResourceNotFoundException("quiz", "id", id));
	}
	
	@DeleteMapping("/courses/{course_id}/quizzes/{id}")
	public ResponseEntity<Quiz> deleteQuiz(@PathVariable("course_id") long course_id,
			@PathVariable("id") long id) {
		if (!courseRepository.existsById(course_id)) {
			throw new ResourceNotFoundException("course", "id", course_id);
		}
		
		quizRepository.findById(id).map(quiz -> {
			quizRepository.delete(quiz);
			return new ResponseEntity<Quiz>(HttpStatus.NO_CONTENT);
		}).orElseThrow(() -> new ResourceNotFoundException("quiz", "id", id));
	}
}
