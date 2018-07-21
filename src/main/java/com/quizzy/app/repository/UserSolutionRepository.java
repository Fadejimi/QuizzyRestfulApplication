package com.quizzy.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizzy.app.model.UserSolution;

@Repository
public interface UserSolutionRepository extends JpaRepository<UserSolution, Long>{
	public List<UserSolution> findUserSolutionsByUserId(long id);
	public List<UserSolution> findUserSolutionsByQuizId(long id);
}
