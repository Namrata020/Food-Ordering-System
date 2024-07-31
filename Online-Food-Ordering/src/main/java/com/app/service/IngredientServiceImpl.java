package com.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.IngredientCategoryDao;
import com.app.dao.IngredientsItemDao;
import com.app.entities.IngredientCategory;
import com.app.entities.IngredientsItem;
import com.app.entities.Restaurant;

@Service
public class IngredientServiceImpl implements IngredientsService {

	@Autowired
	private IngredientsItemDao ingredientsItemDao;
	@Autowired
	private IngredientCategoryDao ingredientCategoryDao;
	@Autowired
	private RestaurantService restaurantService;
	
	
	@Override
	public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {
		Restaurant restaurant=restaurantService.findRestaurantById(restaurantId);
		IngredientCategory category=new IngredientCategory();
		category.setRestaurant(restaurant);
		category.setName(name);
		
		return ingredientCategoryDao.save(category);
	}

	@Override
	public IngredientCategory findIngredientCategoryById(Long id) throws Exception {
		Optional<IngredientCategory> opt=ingredientCategoryDao.findById(id);
		
		if(opt.isEmpty()) {
			throw new Exception("Ingredient Category Not Found");
		}
		
		return opt.get();
	}

	@Override
	public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception {
		restaurantService.findRestaurantById(id);
		
		return ingredientCategoryDao.findByRestaurantId(id);
	}

	@Override
	public IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId)
			throws Exception {
		Restaurant restaurant=restaurantService.findRestaurantById(restaurantId);
		IngredientCategory category=findIngredientCategoryById(categoryId);
		
		IngredientsItem item=new IngredientsItem();
		item.setName(ingredientName);
		item.setRestaurant(restaurant);
		item.setCategory(category);
		
		IngredientsItem ingredient=ingredientsItemDao.save(item);
		category.getIngredients().add(ingredient);
		
		return ingredient;
	}

	@Override
	public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId) {
		return ingredientsItemDao.findByRestaurantId(restaurantId);
	}

	@Override
	public IngredientsItem updateStock(Long id) throws Exception {
		Optional<IngredientsItem> optionalIngredientsItem=ingredientsItemDao.findById(id);
		if(optionalIngredientsItem.isEmpty()) {
			throw new Exception("Ingredient not found!!");
		}
		IngredientsItem ingredientsItem=optionalIngredientsItem.get();
		ingredientsItem.setInStock(!ingredientsItem.isInStock());
		
		return ingredientsItemDao.save(ingredientsItem);
	}

}
