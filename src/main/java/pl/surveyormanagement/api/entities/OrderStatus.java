package pl.surveyormanagement.api.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orderStatus")
public class OrderStatus {
	@Id
	@NotNull
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 255)
	@NotNull
	private EOrderStatus status;

	public OrderStatus() {
		super();
	}

	public OrderStatus(EOrderStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EOrderStatus getStatus() {
		return status;
	}

	public void setStatus(EOrderStatus status) {
		this.status = status;
	}
}
