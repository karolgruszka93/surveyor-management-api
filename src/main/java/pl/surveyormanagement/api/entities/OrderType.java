package pl.surveyormanagement.api.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orderType")
public class OrderType {
	@Id
	@NotNull
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 255)
	@NotNull
	private EOrderType type;

	public OrderType() {
		super();
	}

	public OrderType(EOrderType type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EOrderType getType() {
		return type;
	}

	public void setType(EOrderType type) {
		this.type = type;
	}
}
