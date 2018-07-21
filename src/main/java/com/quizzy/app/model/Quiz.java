package com.quizzy.app.model;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="quizes")
public class Quiz extends AuditModel{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinColumn(name="creator_id", nullable=false)
	@JsonIgnore
	private User creator;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinColumn(name="course_id", nullable=false)
	@JsonIgnore
	private Course course;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}
