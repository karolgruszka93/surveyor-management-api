package pl.surveyormanagement.api.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "vehicles", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class Vehicle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 255)
	private String mark;

	@NotBlank
	@Size(max = 255)
	private String model;

	@NotBlank
	@Size(max = 8)
	private String licensePlateNumber;

	@NotNull
	private LocalDate serviceDate;

	@JsonIgnore
	@NotNull
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "manager_id")
	private User manager;
	@OneToMany(mappedBy = "manager")
	private Set<User> employees = new HashSet<User>();

	public Vehicle() {
		
	}

	public Vehicle(String mark, String model, String licensePlateNumber, LocalDate serviceDate) {
		this.mark = mark;
		this.model = model;
		this.licensePlateNumber = licensePlateNumber;
		this.serviceDate = serviceDate;
	}

	public Vehicle(String mark, String model, String licensePlateNumber, LocalDate serviceDate, User manager) {
		this.mark = mark;
		this.model = model;
		this.licensePlateNumber = licensePlateNumber;
		this.serviceDate = serviceDate;
		this.manager = manager;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public LocalDate getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(LocalDate serviceDate) {
		this.serviceDate = serviceDate;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}
}
