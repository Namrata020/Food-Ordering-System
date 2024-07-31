package com.app.service;

import java.util.List;

import com.app.dto.RestaurantDto;
import com.app.entities.Restaurant;
import com.app.entities.User;
import com.app.request.CreateRestaurantRequest;

public interface RestaurantService {

	public Restaurant createRestaurant(CreateRestaurantRequest req,User user);
	
	public Restaurant updateRestaurant(Long restaurantId,CreateRestaurantRequest updatedRestaurant) throws Exception;
	
	public void deleteRestaurant(Long restaurantId) throws Exception;
	
	//available only for ADMINS
	public List<Restaurant> getAllRestaurants();
	
	public List<Restaurant> searchRestaurant(String keyword);
	
	public Restaurant findRestaurantById(Long id) throws Exception;
	
	public Restaurant getRestaurantByUserId(Long userId) throws Exception;
	
	public RestaurantDto addToFavorites(Long restaurantId,User user) throws Exception;
	
	public Restaurant updateRestaurantStatus(Long id) throws Exception;
	
}
