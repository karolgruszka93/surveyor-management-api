package pl.surveyormanagement.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.surveyormanagement.api.entities.Task;

public interface TaskDAO extends JpaRepository<Task, Long> {
	List<Task> findByManagerId(long managerId);
	
	List<Task> findByUsers_Id(long userId);
}