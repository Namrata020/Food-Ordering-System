package com.app.service;

import java.util.List;

import com.app.entities.IngredientCategory;
import com.app.entities.IngredientsItem;

public interface IngredientsService {
//handles both ingredients category as well as ingredients item
	
	public IngredientCategory createIngredientCategory(String name,Long restaurantId) throws Exception;
	public IngredientCategory findIngredientCategoryById(Long id) throws Exception;
	public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception;
	
	public IngredientsItem createIngredientItem(Long restaurantId,String ingredientName,Long categoryId) throws Exception;
	public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId);
	public IngredientsItem updateStock(Long id) throws Exception;
}
