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

import pl.surveyormanagement.api.dao.TaskDAO;
import pl.surveyormanagement.api.dao.UserDAO;
import pl.surveyormanagement.api.dao.VehicleDAO;
import pl.surveyormanagement.api.entities.User;
import pl.surveyormanagement.api.entities.Vehicle;
import pl.surveyormanagement.api.payload.request.VehiclePostRequest;
import pl.surveyormanagement.api.payload.request.VehicleUpdateRequest;
import pl.surveyormanagement.api.payload.response.MessageResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class VehicleController {
	@Autowired
	UserDAO userDAO;

	@Autowired
	VehicleDAO vehicleDAO;

	@Autowired
	TaskDAO taskDAO;

	@PostMapping("/vehicle")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> createVehicle(@Valid @RequestBody VehiclePostRequest vehiclePostRequest) {
		try {
			String mark = vehiclePostRequest.getMark();
			String model = vehiclePostRequest.getModel();
			String licensePlateNumber = vehiclePostRequest.getLicensePlateNumber();
			LocalDate serviceDate = vehiclePostRequest.getServiceDate();

			User manager = userDAO.findById(vehiclePostRequest.getManagerId())
					.orElseThrow(() -> new RuntimeException("Error: Manager is not found."));

			Vehicle vehicle = new Vehicle(mark, model, licensePlateNumber, serviceDate, manager);

			vehicleDAO.save(vehicle);

			return ResponseEntity.ok(new MessageResponse("Pomyślnie dodano pojazd"));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas dodawania pojazdu"));
		}
	}

	@GetMapping("/vehicle/{managerId}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getVehicles(@Valid @PathVariable long managerId) {
		try {
			List<Vehicle> vehiclesList = new ArrayList<Vehicle>();

			vehicleDAO.findByManagerId(managerId).forEach(vehiclesList::add);

			if (vehiclesList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(vehiclesList, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas pobierania danych"));
		}
	}

	@PutMapping("/vehicle/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> updateVehicle(@Valid @PathVariable long id, @RequestBody VehicleUpdateRequest vehicleUpdateRequest) {
		try {
			Optional<Vehicle> prevVehicle = vehicleDAO.findById(id);

			if (prevVehicle.isPresent()) {
				Vehicle updatedVehicle = prevVehicle.get();
				
				updatedVehicle.setMark(vehicleUpdateRequest.getMark());
				updatedVehicle.setModel(vehicleUpdateRequest.getModel());
				updatedVehicle.setLicensePlateNumber(vehicleUpdateRequest.getLicensePlateNumber());
				updatedVehicle.setServiceDate(vehicleUpdateRequest.getServiceDate());
				
				return new ResponseEntity<>(vehicleDAO.save(updatedVehicle), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas aktualizacji danych"));
		}
	}

	@DeleteMapping("/vehicle/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> deleteVehicle(@Valid @PathVariable long id) {
		try {
			vehicleDAO.deleteById(id);

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Pojazd jest przypisany do istniejącego zadania. Usunięcie niemożliwe"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas usuwania danych"));
		}
	}
}