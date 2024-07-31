package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.IngredientCategory;

public interface IngredientCategoryDao extends JpaRepository<IngredientCategory,Long> {

	List<IngredientCategory> findByRestaurantId(Long id);
}
