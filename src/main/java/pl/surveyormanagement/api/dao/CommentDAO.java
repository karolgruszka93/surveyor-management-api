package pl.surveyormanagement.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.surveyormanagement.api.entities.Comment;

public interface CommentDAO extends JpaRepository<Comment, Long> {
	List<Comment> findByTaskId(long taskId);
	
	List<Comment> findByUserId(long userId);
}
