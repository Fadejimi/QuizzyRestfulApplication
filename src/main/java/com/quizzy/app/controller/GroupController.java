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
import com.quizzy.app.model.Group;
import com.quizzy.app.model.User;
import com.quizzy.app.repository.GroupRepository;
import com.quizzy.app.repository.UserRepository;
import com.quizzy.app.utils.RestUtility;

@RestController
@RequestMapping("/api")
public class GroupController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@GetMapping("/users/{id}/groups")
	public List<Group> getAllGroups(@PathVariable Long id) {
		System.out.println("The user id for the group is " + id);
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("Users", "id", id);
		}
		User user = userRepository.getOne(id);
		return groupRepository.findGroupsByUsers(user);
	}
	
	@PostMapping("/users/{id}/groups")
	public ResponseEntity<Object> createGroup(@PathVariable(value="id") long id, 
			@Valid @RequestBody Group group) {
		return userRepository.findById(id).map(user -> {
			group.setAdmin(user);
			group.getUsers().add(user);
			Group groupResponse =  groupRepository.save(group);
			return RestUtility.getResponseEntity(groupResponse.getId());
		}).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));
	}
	
	@PutMapping("/users/{id}/groups/{group_id}")
	public ResponseEntity<Group> updateGroup(@PathVariable(value="id") long id, 
			@PathVariable(value="group_id") long group_id, 
			@Valid @RequestBody Group groupRequest) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User", "id", id);
		}
		
		return groupRepository.findById(group_id).map(group -> {
			groupRequest.setName(groupRequest.getName());
			Group groupResponse = groupRepository.save(group);
			return new ResponseEntity<Group>(groupResponse, HttpStatus.OK);
		}).orElseThrow(()-> new ResourceNotFoundException("Group", "id", group_id));
	}
	
	@DeleteMapping("/users/{id}/groups/{group_id}")
	public ResponseEntity<Group> deleteGroup(@PathVariable(value="id") long id,
			@PathVariable(value="group_id") long group_id) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User", "id", id);
		}
		
		return groupRepository.findById(group_id).map(group -> {
			groupRepository.deleteById(group_id);
			return new ResponseEntity<Group>(HttpStatus.NO_CONTENT);
		}).orElseThrow(() -> new ResourceNotFoundException("Group", "id", group_id));
	}
}
