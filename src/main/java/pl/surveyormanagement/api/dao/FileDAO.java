package pl.surveyormanagement.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.surveyormanagement.api.entities.File;

public interface FileDAO extends JpaRepository<File, Long> {
	List<File> findByTaskId(long taskId);
}
