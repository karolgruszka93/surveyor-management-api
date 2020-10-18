package pl.surveyormanagement.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.surveyormanagement.api.entities.OrderType;

@Repository
public interface OrderTypeDAO extends JpaRepository<OrderType, Long>{

}
