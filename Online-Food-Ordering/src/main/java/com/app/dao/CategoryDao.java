package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.Category;

public interface CategoryDao extends JpaRepository<Category,Long>{

	public List<Category> findByRestaurantId(Long id);
}
