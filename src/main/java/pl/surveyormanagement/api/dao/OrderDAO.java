package pl.surveyormanagement.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.surveyormanagement.api.entities.Order;

@Repository
public interface OrderDAO extends JpaRepository<Order, Long> {
	List<Order> findByManagerId(long managerId);
}
