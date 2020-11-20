package pl.surveyormanagement.api.payload.request;


import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import pl.surveyormanagement.api.entities.Equipment;
import pl.surveyormanagement.api.entities.Order;
import pl.surveyormanagement.api.entities.User;
import pl.surveyormanagement.api.entities.Vehicle;

public class TaskPostRequest {
	
	private Set<User> employees;
	
	private Set<Order> orders;
	
	private Set<Equipment> equipments;
	
	private Set<Vehicle> vehicles;

	@NotNull
	@JsonFormat(pattern="dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate taskDate;
	
	@NotNull
	private long managerId;

	public Set<User> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<User> employees) {
		this.employees = employees;
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

	public long getManagerId() {
		return managerId;
	}

	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}
	
}
