package pl.surveyormanagement.api.dao;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import pl.surveyormanagement.api.entities.Role;
import pl.surveyormanagement.api.entities.ERole;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
	