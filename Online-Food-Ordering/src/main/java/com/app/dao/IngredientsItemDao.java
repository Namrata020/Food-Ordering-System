package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.IngredientsItem;

public interface IngredientsItemDao extends JpaRepository<IngredientsItem, Long> {

	List<IngredientsItem> findByRestaurantId(Long id);
}
