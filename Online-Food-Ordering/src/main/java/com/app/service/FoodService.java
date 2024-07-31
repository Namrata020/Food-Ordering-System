package com.app.service;

import java.util.List;

import com.app.entities.Category;
import com.app.entities.Food;
import com.app.entities.Restaurant;
import com.app.request.CreateFoodRequest;

public interface FoodService {

	public Food createFood(CreateFoodRequest req,Category category,Restaurant restaurant);
	public void deleteFood(Long foodId) throws Exception;
	public List<Food> getRestaurantFood(Long restaurantId,boolean isVegetarian,boolean isNonveg,boolean isSeasonal,String foodCategory);
	public List<Food> searchFood(String keyword);
	public Food findFoodById(Long foodId) throws Exception;
	public Food updateAvailabilityStatus(Long foodId) throws Exception;


}
