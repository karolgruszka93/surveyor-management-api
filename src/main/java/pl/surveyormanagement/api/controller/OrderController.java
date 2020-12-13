package pl.surveyormanagement.api.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.surveyormanagement.api.dao.OrderDAO;
import pl.surveyormanagement.api.dao.OrderStatusDAO;
import pl.surveyormanagement.api.dao.OrderTypeDAO;
import pl.surveyormanagement.api.dao.UserDAO;
import pl.surveyormanagement.api.entities.Order;
import pl.surveyormanagement.api.entities.OrderStatus;
import pl.surveyormanagement.api.entities.OrderType;

import pl.surveyormanagement.api.entities.User;
import pl.surveyormanagement.api.payload.request.OrderPostRequest;
import pl.surveyormanagement.api.payload.request.OrderUpdateRequest;
import pl.surveyormanagement.api.payload.response.MessageResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class OrderController {

	@Autowired
	UserDAO userDAO;

	@Autowired
	OrderDAO orderDAO;

	@Autowired
	OrderTypeDAO orderTypeDAO;

	@Autowired
	OrderStatusDAO orderStatusDAO;

	@PostMapping("/order")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> createOrder(@Valid @RequestBody OrderPostRequest orderPostRequest) {
		try {
			OrderType orderType = orderTypeDAO.findById(Long.parseLong(orderPostRequest.getType()))
					.orElseThrow(() -> new RuntimeException("Error: Order type is not found."));

			String customer = orderPostRequest.getCustomer();

			LocalDate endDate = orderPostRequest.getEndDate();

			OrderStatus orderStatus = orderStatusDAO.findById(Long.parseLong(orderPostRequest.getStatus()))
					.orElseThrow(() -> new RuntimeException("Error: Order status is not found."));

			User manager = userDAO.findById(orderPostRequest.getManagerId())
					.orElseThrow(() -> new RuntimeException("Error: Manager is not found."));

			Order order = new Order(orderType, customer, endDate, orderStatus, manager);

			orderDAO.save(order);

			return ResponseEntity.ok(new MessageResponse("Pomyślnie dodano zlecenie"));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas dodawania zlecenia"));
		}
	}

	@GetMapping("/order/{managerId}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getOrders(@Valid @PathVariable long managerId) {
		try {
			List<Order> ordersList = new ArrayList<Order>();

			orderDAO.findByManagerId(managerId).forEach(ordersList::add);

			if (ordersList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(ordersList, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas pobierania danych"));
		}
	}

	@PutMapping("/order/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id, @RequestBody OrderUpdateRequest orderUpdateRequest) {
		try {
			Optional<Order> order = orderDAO.findById(id);

			if (order.isPresent()) {
				Order updatedOrder = order.get();

				OrderStatus orderStatus = orderStatusDAO.findById(Long.parseLong(orderUpdateRequest.getStatus()))
						.orElseThrow(() -> new RuntimeException("Error: Order status is not found."));

				updatedOrder.setOrderStatus(orderStatus);

				updatedOrder.setCustomer(orderUpdateRequest.getCustomer());

				updatedOrder.setEndDate(orderUpdateRequest.getEndDate());

				OrderType orderType = orderTypeDAO.findById(Long.parseLong(orderUpdateRequest.getType()))
						.orElseThrow(() -> new RuntimeException("Error: Order type is not found."));

				updatedOrder.setOrderType(orderType);

				return new ResponseEntity<>(orderDAO.save(updatedOrder), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas aktualizacji danych"));
		}
	}

	@DeleteMapping("/order/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> deleteOrder(@Valid @PathVariable long id) {
		try {
			orderDAO.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Zlecenie jest przypisane do istniejącego zadania. Usunięcie niemożliwe"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas usuwania danych"));
		}
	}
}
