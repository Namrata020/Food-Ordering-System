package com.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.CategoryDao;
import com.app.entities.Category;
import com.app.entities.Restaurant;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Override
	public Category createCategory(String name, Long userId) throws Exception {
		Restaurant restaurant=restaurantService.getRestaurantByUserId(userId);
		Category category=new Category();
		category.setName(name);
		category.setRestaurant(restaurant);
		
		return categoryDao.save(category);
	}

	@Override
	public List<Category> findCategoryByRestaurantId(Long id) throws Exception {
		Restaurant restaurant=restaurantService.getRestaurantByUserId(id);
		return categoryDao.findByRestaurantId(restaurant.getId());
	}

	@Override
	public Category findCategoryById(Long id) throws Exception {
		Optional<Category> optionalCategory=categoryDao.findById(id);
		
		if(optionalCategory.isEmpty()) {
			throw new Exception("Category not found!!");
		}
		
		return optionalCategory.get();
	}

}
