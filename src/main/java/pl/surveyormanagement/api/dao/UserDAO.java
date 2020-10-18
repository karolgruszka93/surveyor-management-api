package pl.surveyormanagement.api.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.surveyormanagement.api.entities.User;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByFirstName(String firstName);
	
	Optional<User> findByLastName(String lastName);
	
	List<User> findByManagerId(long managerId);

	Boolean existsByEmail(String email);
}
