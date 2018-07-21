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
import com.quizzy.app.model.Option;
import com.quizzy.app.repository.OptionRepository;
import com.quizzy.app.repository.QuestionRepository;
import com.quizzy.app.utils.RestUtility;

@RestController
@RequestMapping("/api")
public class OptionController {
	
	@Autowired
	OptionRepository optionRepository;
	
	@Autowired
	QuestionRepository questionRepository;
	
	@GetMapping("/questions/{id}/options")
	public List<Option> getOptions(@PathVariable("id") long id) {
		return optionRepository.findOptionsByQuestionId(id);
	}
	
	@PostMapping("/questions/{id}/options")
	public ResponseEntity<Object> createOption(@PathVariable("id") long id,
			@Valid @RequestBody Option option) {
		questionRepository.findById(id).map(question -> {
			option.setQuestion(question);
			Option o = optionRepository.save(option);
			
			return RestUtility.getResponseEntity(o.getId());
		}).orElseThrow(() -> new ResourceNotFoundException("question", "id", id));
	}
	
	@PutMapping("/questions/{question_id}/options/{id}")
	public ResponseEntity<Option> updateOption(@PathVariable("question_id") long question_id, 
			@PathVariable("id") long id,
			@Valid @RequestBody Option optionRequest) {
		if (!questionRepository.existsById(question_id)) {
			throw new ResourceNotFoundException("question", "id", question_id);
		}
		
		optionRepository.findById(id).map(option -> {
			option.setAnswer_flag(optionRequest.isAnswer_flag());
			option.setOption(optionRequest.getOption());
			
			Option o = optionRepository.save(option);
			return new ResponseEntity<Option>(o, HttpStatus.OK);
		}).orElseThrow(() -> new ResourceNotFoundException("option", "id", id));
	}
	
	@GetMapping("/questions/{question_id}/options/{id}")
	public Option getOption(@PathVariable("question_id") long question_id, 
			@PathVariable("id") long id) {
		if (!questionRepository.existsById(id)) {
			throw new ResourceNotFoundException("question", "id", question_id);
		}
		
		return optionRepository.findById(id).orElseThrow(() -> 
				new ResourceNotFoundException("option", "id", id));
	}
	
	@DeleteMapping("/questions/{question_id}/options/{id}")
	public ResponseEntity<Option> deleteOption(@PathVariable("question_id") long question_id,
			@PathVariable("id") long id) {
		if (!questionRepository.existsById(id)) {
			throw new ResourceNotFoundException("question", "id", question_id);
		}
		
		optionRepository.findById(id).map(option -> {
			optionRepository.delete(option);
			
			return new ResponseEntity<Option>(HttpStatus.NO_CONTENT);
		}).orElseThrow(() -> new ResourceNotFoundException("option", "id", id));
	}
}
