package com.quizzy.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizzy.app.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{
	public List<Course> findByGroupId(long id);
	public List<Course> findByCreatorId(long id);
}
