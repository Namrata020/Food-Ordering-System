package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.entities.Food;

public interface FoodDao extends JpaRepository<Food,Long> {

	public List<Food> findByRestaurantId(Long restaurantId);
	
	@Query("SELECT f FROM Food WHERE f.name LIKE %:keyword% OR f.foodCategory.name LIKE %:keyword%")
	public List<Food> searchFood(@Param("keyword") String keyword);
}
