package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entities.IngredientCategory;
import com.app.entities.IngredientsItem;
import com.app.request.IngredientCategoryRequest;
import com.app.request.IngredientRequest;
import com.app.service.IngredientsService;

@RestController
@RequestMapping("/api/admin/ingredients")
public class IngredientController {

	@Autowired
	private IngredientsService ingredientsService;
	
	@PostMapping("/category")
	public ResponseEntity<IngredientCategory> createIngredientCategory(@RequestBody IngredientCategoryRequest req) throws Exception{
		IngredientCategory item=ingredientsService.createIngredientCategory(req.getName(),req.getRestaurantId());

		return new ResponseEntity<IngredientCategory>(item,HttpStatus.CREATED);
	}
	
	@PostMapping()
	public ResponseEntity<IngredientsItem> createIngredientsItem(@RequestBody IngredientRequest req) throws Exception{
		IngredientsItem item=ingredientsService.createIngredientItem(req.getRestaurantId(),req.getName(),req.getCategoryId());

		return new ResponseEntity<>(item,HttpStatus.CREATED);
	}
	
	//Method to update stock
	@PutMapping("/{id}/stock")
	public ResponseEntity<IngredientsItem> updateIngredientStock(@PathVariable Long id) throws Exception{
		IngredientsItem item=ingredientsService.updateStock(id);
		return new ResponseEntity<IngredientsItem>(item,HttpStatus.OK);
	}
	
	//get all ingredients of a restaurant
	@GetMapping("/restaurant/{id}")
	public ResponseEntity<List<IngredientsItem>> getRestaurantIngredients(@PathVariable Long id) throws Exception{
		List<IngredientsItem> items=ingredientsService.findRestaurantsIngredients(id);
		
		return new ResponseEntity<>(items,HttpStatus.OK);
	}
	
	//to find all the category of any restaurant
	@GetMapping("/restaurant/{id}/category")
	public ResponseEntity<List<IngredientCategory>> getRestaurantIngredientCategory(@PathVariable Long id) throws Exception{
		List<IngredientCategory> items=ingredientsService.findIngredientCategoryByRestaurantId(id);
		
		return new ResponseEntity<>(items,HttpStatus.OK);
	}
	
	
	
	
}
