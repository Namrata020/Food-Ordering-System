package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.Order;

public interface OrderDao extends JpaRepository<Order,Long> {

	public List<Order> findByCustomerId(Long userId);
	public List<Order> findByRestaurantId(Long restaurantId);
	
	
}
