package pl.surveyormanagement.api.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "equipmentType")
public class EquipmentType {
	@Id
	@NotNull
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 255)
	@NotNull
	private EEquipmentType type;

	public EquipmentType() {
		super();
	}

	public EquipmentType(EEquipmentType type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EEquipmentType getType() {
		return type;
	}

	public void setType(EEquipmentType type) {
		this.type = type;
	}
}
