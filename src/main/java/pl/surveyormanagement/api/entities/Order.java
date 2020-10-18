package pl.surveyormanagement.api.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "orders", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@OneToOne(cascade = CascadeType.REFRESH)
	@JoinTable(name = "orders_orderType", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "orderType_id"))
	private OrderType orderType;

	@NotBlank
	@Size(max = 255)
	private String customer;

	@NotNull
	private LocalDate endDate;

	@NotNull
	@OneToOne(cascade = CascadeType.REFRESH)
	@JoinTable(name = "orders_orderStatus", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "orderStatus_id"))
	private OrderStatus orderStatus;

	@JsonIgnore
	@NotNull
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "manager_id")
	private User manager;
	@OneToMany(mappedBy = "manager")
	private Set<User> employees = new HashSet<User>();

	public Order() {

	}

	public Order(OrderType orderType, String customer, LocalDate endDate, OrderStatus orderStatus) {
		this.orderType = orderType;
		this.customer = customer;
		this.endDate = endDate;
		this.orderStatus = orderStatus;
	}

	public Order(OrderType orderType, String customer, LocalDate endDate, OrderStatus orderStatus, User manager) {
		this.orderType = orderType;
		this.customer = customer;
		this.endDate = endDate;
		this.orderStatus = orderStatus;
		this.manager = manager;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}
}
