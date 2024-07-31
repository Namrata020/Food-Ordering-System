package com.app.request;

import java.util.List;

import com.app.entities.Category;
import com.app.entities.IngredientsItem;

import lombok.Data;

@Data
public class CreateFoodRequest {

	private String name;
	private String description;
	private Long price;
	
	private Category category;
	private List<String> images;
	private Long restaurantId;
	private boolean vegetarian;
	private boolean seasonal;
	private List<IngredientsItem> ingredients;
	
}
