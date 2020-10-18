package pl.surveyormanagement.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.surveyormanagement.api.dao.EquipmentDAO;
import pl.surveyormanagement.api.dao.EquipmentTypeDAO;
import pl.surveyormanagement.api.dao.UserDAO;
import pl.surveyormanagement.api.entities.Equipment;
import pl.surveyormanagement.api.entities.EquipmentType;
import pl.surveyormanagement.api.entities.User;
import pl.surveyormanagement.api.payload.request.EquipmentPostRequest;
import pl.surveyormanagement.api.payload.request.EquipmentUpdateRequest;
import pl.surveyormanagement.api.payload.response.MessageResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class EquipmentController {

	@Autowired
	EquipmentTypeDAO equipmentTypeDAO;

	@Autowired
	EquipmentDAO equipmentDAO;

	@Autowired
	UserDAO userDAO;

	@PostMapping("/equipment")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> createEquipment(@Valid @RequestBody EquipmentPostRequest equipmentPostRequest) {
		try {

			String name = equipmentPostRequest.getName();

			String model = equipmentPostRequest.getModel();

			EquipmentType equipmentType = equipmentTypeDAO.findById(Long.parseLong(equipmentPostRequest.getType()))
					.orElseThrow(() -> new RuntimeException("Error: Order type is not found."));

			User manager = userDAO.findById(equipmentPostRequest.getManagerId())
					.orElseThrow(() -> new RuntimeException("Error: Manager is not found."));

			Equipment equipment = new Equipment(name, model, equipmentType, manager);

			equipmentDAO.save(equipment);

			return ResponseEntity.ok(new MessageResponse("Pomyślnie dodano sprzęt"));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas dodawania sprzętu"));
		}
	}

	@GetMapping("/equipment/{managerId}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getEquipments(@Valid @PathVariable long managerId) {
		try {
			List<Equipment> equipmentList = new ArrayList<Equipment>();

			equipmentDAO.findByManagerId(managerId).forEach(equipmentList::add);

			if (equipmentList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(equipmentList, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas pobierania danych"));
		}
	}

	@PutMapping("/equipment/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> updateEquipment(@Valid @PathVariable Long id, @RequestBody EquipmentUpdateRequest equipmentUpdateRequest) {
		try {
			Optional<Equipment> equipment = equipmentDAO.findById(id);

			if (equipment.isPresent()) {
				Equipment updatedEquipment = equipment.get();

				EquipmentType equipmentType = equipmentTypeDAO.findById(Long.parseLong(equipmentUpdateRequest.getType()))
						.orElseThrow(() -> new RuntimeException("Error: Order type is not found."));

				updatedEquipment.setEquipmentType(equipmentType);

				updatedEquipment.setModel(equipmentUpdateRequest.getModel());

				updatedEquipment.setName(equipmentUpdateRequest.getName());

				return new ResponseEntity<>(equipmentDAO.save(updatedEquipment), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas aktualizacji danych"));
		}
	}

	@DeleteMapping("/equipment/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> deleteEquipment(@Valid @PathVariable long id) {
		try {
			equipmentDAO.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas usuwania danych"));
		}
	}
}
