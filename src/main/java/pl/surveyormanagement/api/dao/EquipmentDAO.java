package pl.surveyormanagement.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.surveyormanagement.api.entities.Equipment;

@Repository
public interface EquipmentDAO extends JpaRepository<Equipment, Long>{
	List<Equipment> findByManagerId(long managerId);
}

