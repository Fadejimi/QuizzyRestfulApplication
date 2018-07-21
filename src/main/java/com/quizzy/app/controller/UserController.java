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
import com.quizzy.app.model.User;
import com.quizzy.app.repository.UserRepository;
import com.quizzy.app.utils.RestUtility;

@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	// get all users
	@GetMapping("/users")
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	// get a user
	@GetMapping("/users/{id}")
	public User getUser(@PathVariable(value="id") long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
	}
	
	// post a user
	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User userResponse = userRepository.save(user);
		return RestUtility.getResponseEntity(userResponse.getId());
	}
	
	// update a user
	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable(value="id") long id, 
			@Valid @RequestBody User user) {
		User oldUser = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		//oldUser.setEmail(user.getEmail());
		oldUser.setPassword(user.getPassword());
		oldUser.setName(user.getName());
		
		User userResponse = userRepository.save(oldUser);
		return new ResponseEntity<User>(userResponse, HttpStatus.OK);
	}
	
	// delete a user
	@DeleteMapping("/users/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable(value="id") long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		
		userRepository.delete(user);
		
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
}
