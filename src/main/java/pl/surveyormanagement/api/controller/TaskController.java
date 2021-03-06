package pl.surveyormanagement.api.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.surveyormanagement.api.dao.TaskDAO;
import pl.surveyormanagement.api.dao.UserDAO;
import pl.surveyormanagement.api.entities.Equipment;
import pl.surveyormanagement.api.entities.Order;
import pl.surveyormanagement.api.entities.Task;
import pl.surveyormanagement.api.entities.User;
import pl.surveyormanagement.api.entities.Vehicle;
import pl.surveyormanagement.api.payload.request.TaskPostRequest;
import pl.surveyormanagement.api.payload.response.MessageResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class TaskController {

	@Autowired
	TaskDAO taskDAO;

	@Autowired
	UserDAO userDAO;

	@PostMapping("/task")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> createTask(@Valid @RequestBody TaskPostRequest taskPostRequest) {
		try {
			Set<User> employees = new HashSet<User>();
			employees.addAll(taskPostRequest.getEmployees());

			Set<Order> orders = new HashSet<Order>();
			orders.addAll(taskPostRequest.getOrders());

			Set<Equipment> equipments = new HashSet<Equipment>();
			equipments.addAll(taskPostRequest.getEquipments());

			Set<Vehicle> vehicles = new HashSet<Vehicle>();
			vehicles.addAll(taskPostRequest.getVehicles());

			LocalDate taskDate = taskPostRequest.getTaskDate();

			User manager = userDAO.findById(taskPostRequest.getManagerId())
					.orElseThrow(() -> new RuntimeException("Error: Manager is not found."));

			Task task = new Task(employees, orders, equipments, vehicles, taskDate, manager);

			taskDAO.save(task);

			return ResponseEntity.ok(new MessageResponse("Pomyślnie dodano zadanie"));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas dodawania zadania"));
		}
	}

	@GetMapping("/tasks/{managerId}/{taskDate}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getTasks(@Valid @PathVariable long managerId, @PathVariable LocalDate taskDate) {
		try {
			List<Task> tasksList = new ArrayList<Task>();

			taskDAO.findByManagerId(managerId).forEach(tasksList::add);

			List<Task> filteredTasksList = filterTasksListByDate(tasksList, taskDate);

			return new ResponseEntity<>(filteredTasksList, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas pobierania danych"));
		}
	}

	@DeleteMapping("/task/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> deleteTask(@Valid @PathVariable long id) {
		try {
			taskDAO.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas usuwania danych"));
		}
	}
	
	@GetMapping("/tasks/employee/{userId}/{taskDate}")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<?> getEmployeeTasks(@Valid @PathVariable long userId, @PathVariable LocalDate taskDate) {
		try {
			List<Task> tasksList = taskDAO.findByUsers_Id(userId);

			if (tasksList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			List<Task> filteredTasksList = filterTasksListByDate(tasksList, taskDate);

			return new ResponseEntity<>(filteredTasksList, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas pobierania danych"));
		}
	}

	public List<Task> filterTasksListByUser(User user)
	{
		List<Task> tasksList = new ArrayList<Task>();
		
		taskDAO.findByUsers_Id(user.getId()).forEach(tasksList::add);
		
		return tasksList;
	}
	
	public List<Task> filterTasksListByDate(List<Task> tasksList, LocalDate taskDate)
	{
		List<Task> filteredTaskList = new ArrayList<Task>();

		for (Task task : tasksList) {
			if (task.getTaskDate().isEqual(taskDate)) {
				filteredTaskList.add(task);
			}
		}
		
		return filteredTaskList;
	}
}
