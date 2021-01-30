package pl.surveyormanagement.api.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tasks", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tasks_users", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tasks_orders", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "orders_id"))
	private Set<Order> orders = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tasks_equipments", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "equipment_id"))
	private Set<Equipment> equipments = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tasks_vehicles", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
	private Set<Vehicle> vehicles = new HashSet<>();

	@NotNull
	private LocalDate taskDate;

	@JsonIgnore
	@NotNull
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "manager_id")
	private User manager;
	@JsonIgnore
	@OneToMany(mappedBy = "manager")
	private Set<User> employees = new HashSet<User>();

	public Task() {

	}

	public Task(Set<User> users, Set<Order> orders, Set<Equipment> equipments, Set<Vehicle> vehicles,
			LocalDate taskDate, User manager) {
		this.users = users;
		this.orders = orders;
		this.equipments = equipments;
		this.vehicles = vehicles;
		this.taskDate = taskDate;
		this.manager = manager;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public Set<Equipment> getEquipments() {
		return equipments;
	}

	public void setEquipments(Set<Equipment> equipments) {
		this.equipments = equipments;
	}

	public Set<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Set<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public LocalDate getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(LocalDate taskDate) {
		this.taskDate = taskDate;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public Set<User> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<User> employees) {
		this.employees = employees;
	}
}
