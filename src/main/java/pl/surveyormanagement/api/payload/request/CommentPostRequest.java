package pl.surveyormanagement.api.payload.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CommentPostRequest {

	@NotBlank
    @Size(max = 160)
    private String comment;
	
	@NotNull
	@JsonFormat(pattern="dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate commentDate;
	
	@NotNull
	private long taskId;
	
	@NotNull
	private long userId;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDate getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(LocalDate commentDate) {
		this.commentDate = commentDate;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
}
