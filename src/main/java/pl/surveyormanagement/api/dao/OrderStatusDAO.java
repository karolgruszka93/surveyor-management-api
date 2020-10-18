package pl.surveyormanagement.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.surveyormanagement.api.entities.OrderStatus;

@Repository
public interface OrderStatusDAO extends JpaRepository<OrderStatus, Long>{

}
