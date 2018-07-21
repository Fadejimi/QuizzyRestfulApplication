package com.quizzy.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizzy.app.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>{
	//Page<Group> findByAdminId(Long adminId, Pageable pageable);
	List<Group> findByAdminId(long adminId);
	
	List<Group> findGroupsByUsers(long userId);
}
