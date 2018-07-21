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
import com.quizzy.app.model.Course;
import com.quizzy.app.repository.CourseRepository;
import com.quizzy.app.repository.GroupRepository;
import com.quizzy.app.repository.UserRepository;
import com.quizzy.app.utils.RestUtility;

@RestController
@RequestMapping("/api")
public class CourseController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	GroupRepository groupRepository;
	
	@GetMapping("/group/{id}/courses")
	public List<Course> getCourses(@PathVariable("id") long id) {
		return courseRepository.findByGroupId(id); 
	}
	
	@PostMapping("/group/{id}/courses")
	public ResponseEntity<Object> createCourse(@PathVariable("id") long id, 
			@Valid @RequestBody Course course) {
		return groupRepository.findById(id).map( group -> {
			course.setGroup(group);
			Course courseResponse = courseRepository.save(course);
			return RestUtility.getResponseEntity(courseResponse.getId());
		}).orElseThrow(() -> new ResourceNotFoundException("Group", "id", id));
	}
	
	@PutMapping("/group/{id}/courses/{course_id}")
	public ResponseEntity<Course> updateCourse(@PathVariable("id") long id,
			@PathVariable("course_id") long course_id, 
			@Valid @RequestBody Course requestCourse) {
		if (!groupRepository.existsById(id)) {
			throw new ResourceNotFoundException("Group", "Id", id);
		}
		
		return courseRepository.findById(course_id).map(course -> {
			course.setName(requestCourse.getName());
			Course c = courseRepository.save(course);
			
			return new ResponseEntity<Course>(c, HttpStatus.OK);
		}).orElseThrow(() -> new ResourceNotFoundException("Course", "Id", id));
	}
	
	@DeleteMapping("/group/{id}/courses/{course_id")
	public ResponseEntity<Course> deleteCourse(@PathVariable("id") long id,
			@PathVariable("course_id") long course_id) {
		if (!groupRepository.existsById(id)) {
			throw new ResourceNotFoundException("Group", "Id", id);
		}
		
		return courseRepository.findById(course_id).map(course -> {
			courseRepository.delete(course);
			return new ResponseEntity<Course>(HttpStatus.NO_CONTENT);
		}).orElseThrow(() -> new ResourceNotFoundException("Course", "Id", id));
	}
}
