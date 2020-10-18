package pl.surveyormanagement.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.surveyormanagement.api.entities.Vehicle;

@Repository
public interface VehicleDAO extends JpaRepository<Vehicle, Long> {
	List<Vehicle> findByManagerId(long managerId);
}
