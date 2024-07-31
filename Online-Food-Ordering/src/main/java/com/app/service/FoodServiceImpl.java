package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.FoodDao;
import com.app.entities.Category;
import com.app.entities.Food;
import com.app.entities.Restaurant;
import com.app.request.CreateFoodRequest;

@Service
public class FoodServiceImpl implements FoodService {

	@Autowired
	private FoodDao foodDao;
	
	@Override
	public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) {
		Food food=new Food();
		food.setFoodCategory(category);
		food.setRestaurant(restaurant);
		food.setDescription(req.getDescription());
		food.setImages(req.getImages());
		food.setName(req.getName());
		food.setPrice(req.getPrice());
		food.setIngredients(req.getIngredients());
		food.setSeasonal(req.isSeasonal());
		food.setVegetarian(req.isVegetarian());
		
		Food savedFood=foodDao.save(food);
		restaurant.getFoods().add(savedFood);
		
		return savedFood;
	}

	@Override
	public void deleteFood(Long foodId) throws Exception {
		Food food=findFoodById(foodId);
		food.setRestaurant(null);
		foodDao.save(food);

	}

	@Override
	public List<Food> getRestaurantFood(Long restaurantId, boolean isVegetarian, boolean isNonveg, boolean isSeasonal,
			String foodCategory) {
		List<Food> foods=foodDao.findByRestaurantId(restaurantId);
		
		if(isVegetarian) {
			foods=filterByVegetarian(foods,isVegetarian);
		}
		if(isNonveg) {
			foods=filterByNonVeg(foods,isNonveg);
		}
		if(isSeasonal) {
			foods=filterBySeasonal(foods,isSeasonal);
		}
		if(foodCategory!=null && !foodCategory.equals(" ")) {
			foods=filterByCategory(foods,foodCategory);
		}
		
		return foods;
	}

	private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
		return foods
				.stream()
				.filter(food -> {
					if(food.getFoodCategory()!=null) {
						return food.getFoodCategory().getName().equals(foodCategory);
					}
					return false;
				})
				.collect(Collectors.toList());
	}

	private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasonal) {
		return foods
				.stream()
				.filter(food -> food.isSeasonal()==isSeasonal)
				.collect(Collectors.toList());
	}

	private List<Food> filterByNonVeg(List<Food> foods, boolean isNonveg) {
		return foods
				.stream()
				.filter(food -> food.isVegetarian()==false)
				.collect(Collectors.toList());
	}

	private List<Food> filterByVegetarian(List<Food> foods, boolean isVegetarian) {
		return foods
				.stream()
				.filter(food -> food.isVegetarian()==isVegetarian)
				.collect(Collectors.toList());
	}

	@Override
	public List<Food> searchFood(String keyword) {
		return foodDao.searchFood(keyword);
	}

	@Override
	public Food findFoodById(Long foodId) throws Exception {
		Optional<Food> optionalFood=foodDao.findById(foodId);
		
		if(optionalFood.isEmpty()) {
			throw new Exception("No such food exists...");
		}
		
		return optionalFood.get();
	}

	@Override
	public Food updateAvailabilityStatus(Long foodId) throws Exception {
		Food food=findFoodById(foodId);
		food.setAvailable(!food.isAvailable());
		
		return foodDao.save(food);
	}

}
