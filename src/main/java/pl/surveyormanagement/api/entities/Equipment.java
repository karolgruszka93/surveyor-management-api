package pl.surveyormanagement.api.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "equipment", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class Equipment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 255)
	private String name;

	@NotBlank
	@Size(max = 255)
	private String model;

	@NotNull
	@OneToOne(cascade = CascadeType.REFRESH)
	@JoinTable(name = "equipment_equipmentType", joinColumns = @JoinColumn(name = "equipment_id"), inverseJoinColumns = @JoinColumn(name = "equipmentType_id"))
	private EquipmentType equipmentType;

	@JsonIgnore
	@NotNull
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "manager_id")
	private User manager;
	@OneToMany(mappedBy = "manager")
	private Set<User> employees = new HashSet<User>();

	public Equipment() {

	}

	public Equipment(String name, String model, EquipmentType equipmentType) {
		this.name = name;
		this.model = model;
		this.equipmentType = equipmentType;
	}

	public Equipment(String name, String model, EquipmentType equipmentType, User manager) {
		this.name = name;
		this.model = model;
		this.equipmentType = equipmentType;
		this.manager = manager;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}
}
