package pl.surveyormanagement.api.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.surveyormanagement.api.dao.CommentDAO;
import pl.surveyormanagement.api.dao.TaskDAO;
import pl.surveyormanagement.api.dao.UserDAO;
import pl.surveyormanagement.api.entities.Comment;
import pl.surveyormanagement.api.entities.Task;
import pl.surveyormanagement.api.entities.User;
import pl.surveyormanagement.api.payload.request.CommentPostRequest;
import pl.surveyormanagement.api.payload.response.MessageResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CommentController {
	
	@Autowired
	TaskDAO taskDAO;

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	CommentDAO commentDAO;
	
	@PostMapping("/comment")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> createComment(@Valid @RequestBody CommentPostRequest commentPostRequest) {
		try {
			String commentValue = commentPostRequest.getComment();

			LocalDate commentDate = commentPostRequest.getCommentDate();

			Task task = taskDAO.findById(commentPostRequest.getTaskId())
					.orElseThrow(() -> new RuntimeException("Error: Task is not found."));
			
			User user = userDAO.findById(commentPostRequest.getUserId())
					.orElseThrow(() -> new RuntimeException("Error: User is not found."));

			Comment comment = new Comment(commentValue, commentDate, task, user);

			commentDAO.save(comment);

			return ResponseEntity.ok(new MessageResponse("Pomyślnie dodano komentarz"));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas zapisu komentarza"));
		}
	}
	
	@GetMapping("/comments/{taskId}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getComments(@Valid @PathVariable long taskId) {
		try {
			List<Comment> commentsList = new ArrayList<Comment>();

			commentDAO.findByTaskId(taskId).forEach(commentsList::add);
			
			return new ResponseEntity<>(commentsList, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas pobierania danych"));
		}
	}

}
