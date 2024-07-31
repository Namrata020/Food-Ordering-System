package com.app.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.AddressDao;
import com.app.dao.RestaurantDao;
import com.app.dao.UserDao;
import com.app.dto.RestaurantDto;
import com.app.entities.Address;
import com.app.entities.Restaurant;
import com.app.entities.User;
import com.app.request.CreateRestaurantRequest;

@Service
public class RestaurantServiceImpl implements RestaurantService{

	@Autowired
	private RestaurantDao restaurantDao;
	@Autowired
	private AddressDao addressDao;
	@Autowired
	private UserDao userDao;
	
	@Override
	public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {
		Address address=addressDao.save(req.getAddress());
		Restaurant restaurant=new Restaurant();
		restaurant.setAddress(address);
		restaurant.setContactInformation(req.getContactInformation());
		restaurant.setCuisineType(req.getCuisineType());
		restaurant.setDescription(req.getDescription());
		restaurant.setImages(req.getImages());
		restaurant.setName(req.getName());
		restaurant.setOpeningHours(req.getOpeningHours());
		restaurant.setRegistrationDate(LocalDateTime.now());
		restaurant.setOwner(user);
		
		return restaurantDao.save(restaurant);
	}

	@Override
	public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {
		Restaurant restaurant=findRestaurantById(restaurantId);
		
		if(restaurant.getCuisineType()!=null) {
			restaurant.setCuisineType(updatedRestaurant.getCuisineType());
		}
		if(restaurant.getDescription()!=null) {
			restaurant.setDescription(updatedRestaurant.getDescription());
		}
		if(restaurant.getName()!=null) {
			restaurant.setName(updatedRestaurant.getName());
		}
		
		return restaurantDao.save(restaurant);
	}

	@Override
	public void deleteRestaurant(Long restaurantId) throws Exception {
		Restaurant restaurant=findRestaurantById(restaurantId);
		restaurantDao.delete(restaurant);
		
	}

	@Override
	public List<Restaurant> getAllRestaurants() {

		return restaurantDao.findAll();
	}

	@Override
	public List<Restaurant> searchRestaurant(String keyword) {
		
		return restaurantDao.findBySearchQuery(keyword);
	}

	@Override
	public Restaurant findRestaurantById(Long id) throws Exception {
		Optional<Restaurant> opt=restaurantDao.findById(id);
		
		if(opt.isEmpty()) {
			throw new Exception("Restaurant not found with id: "+id);
		}
		
		return opt.get();
	}

	@Override
	public Restaurant getRestaurantByUserId(Long userId) throws Exception {
		Restaurant restaurant=restaurantDao.findByOwnerId(userId);
		
		if(restaurant==null) {
			throw new Exception("Restaurant not found with owner Id: "+userId);
		}
		
		return restaurant;
	}

	@Override
	public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception {
		
		 Restaurant restaurant = findRestaurantById(restaurantId);

		    RestaurantDto dto = new RestaurantDto();
		    dto.setDescription(restaurant.getDescription());
		    dto.setImages(restaurant.getImages());
		    dto.setTitle(restaurant.getName());
		    dto.setId(restaurantId);

		    boolean isFavourite = false;
		    List<Restaurant> favourites = user.getFavourites();
		    for (Restaurant favourite : favourites) {
		        if (favourite.getId().equals(restaurantId)) {
		            isFavourite = true;
		            favourites.remove(favourite); // Remove the restaurant object from favourites
		            break;
		        }
		    }

		    if (isFavourite) {
		        // Unfavorite scenario: remove restaurant from favourites
		    } else {
		        // Favorite scenario: add restaurant to favourites (assuming RestaurantDto has required fields)
		        favourites.add(restaurant); // Add the actual Restaurant object
		    }

		    userDao.save(user);

		    return dto;
		}

	@Override
	public Restaurant updateRestaurantStatus(Long id) throws Exception {
		Restaurant restaurant=findRestaurantById(id);
		restaurant.setOpen(!restaurant.isOpen());
		return restaurantDao.save(restaurant);
	}

}
