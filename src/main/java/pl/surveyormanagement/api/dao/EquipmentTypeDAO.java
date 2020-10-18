package pl.surveyormanagement.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.surveyormanagement.api.entities.EquipmentType;

@Repository
public interface EquipmentTypeDAO extends JpaRepository<EquipmentType, Long>{

}

