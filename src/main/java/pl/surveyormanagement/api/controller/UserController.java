package pl.surveyormanagement.api.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.surveyormanagement.api.dao.RoleDAO;
import pl.surveyormanagement.api.dao.UserDAO;
import pl.surveyormanagement.api.entities.ERole;
import pl.surveyormanagement.api.entities.Role;
import pl.surveyormanagement.api.entities.User;
import pl.surveyormanagement.api.payload.request.LoginRequest;
import pl.surveyormanagement.api.payload.request.RegisterRequest;
import pl.surveyormanagement.api.payload.request.UserUpdateRequest;
import pl.surveyormanagement.api.payload.response.JwtResponse;
import pl.surveyormanagement.api.payload.response.MessageResponse;
import pl.surveyormanagement.api.security.jwt.JwtUtils;
import pl.surveyormanagement.api.security.services.UserDetailsImpl;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserDAO userDAO;

	@Autowired
	RoleDAO roleDAO;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		try {
			if (userDAO.existsByEmail(registerRequest.getEmail())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Istnieje konto o podanym adresie email"));
			}

			User user = new User(registerRequest.getEmail(), registerRequest.getFirstName(),
					registerRequest.getLastName(), encoder.encode(registerRequest.getPassword()));

			Set<Role> roles = new HashSet<>();

			Role managerRole = roleDAO.findByName(ERole.ROLE_MANAGER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));

			roles.add(managerRole);

			user.setRoles(roles);

			userDAO.save(user);

			return ResponseEntity.ok(new MessageResponse("Rejestracja przebiegła pomyślnie"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas tworzenia konta"));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());

			return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas logowania"));
		}
	}

	@PostMapping("/employee")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> createEmployee(@Valid @RequestBody RegisterRequest registerRequest) {
		try {
			if (userDAO.existsByEmail(registerRequest.getEmail())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Istnieje pracownik o podanym adresie email"));
			}

			User user = new User(registerRequest.getEmail(), registerRequest.getFirstName(),
					registerRequest.getLastName(), encoder.encode(registerRequest.getPassword()));

			Set<Role> roles = new HashSet<>();

			Role employeeRole = roleDAO.findByName(ERole.ROLE_EMPLOYEE)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));

			User manager = userDAO.findById(registerRequest.getManagerId())
					.orElseThrow(() -> new RuntimeException("Error: Manager is not found."));

			roles.add(employeeRole);

			user.setRoles(roles);

			user.setManager(manager);

			userDAO.save(user);

			return ResponseEntity.ok(new MessageResponse("Pomyślnie dodano pracownika"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas dodawania pracownika"));
		}
	}

	@GetMapping("/employee/{managerId}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getEmployees(@Valid @PathVariable long managerId) {
		try {
			List<User> employeesList = new ArrayList<User>();

			userDAO.findByManagerId(managerId).forEach(employeesList::add);

			if (employeesList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(employeesList, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas pobierania danych"));
		}
	}

	@PutMapping("/employee/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> updateEmployee(@Valid @PathVariable long id, @RequestBody UserUpdateRequest userUpdateRequest) {
		try {
			Optional<User> prevUser = userDAO.findById(id);

			if (prevUser.isPresent()) {
				User updatedUser = prevUser.get();

				updatedUser.setFirstName(userUpdateRequest.getFirstName());
				updatedUser.setLastName(userUpdateRequest.getLastName());

				if (!userUpdateRequest.getEmail().equalsIgnoreCase(updatedUser.getEmail())) {
					if (userDAO.existsByEmail(userUpdateRequest.getEmail())) {
						return ResponseEntity.badRequest()
								.body(new MessageResponse("Istnieje użytkownik o podanym adresie email"));
					}
					updatedUser.setEmail(userUpdateRequest.getEmail());
				}

				return new ResponseEntity<>(userDAO.save(updatedUser), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas aktualizacji danych"));
		}
	}

	@DeleteMapping("/employee/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> deleteEmployee(@Valid @PathVariable long id) {
		try {
			userDAO.deleteById(id);

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas usuwania danych"));
		}
	}
}
