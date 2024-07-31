package com.app.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsItem {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@ManyToOne //multiple ingredients in one category
	private IngredientCategory category;
	
	@JsonIgnore
	@ManyToOne //one restaurant has multiple ingredient
	private Restaurant restaurant;
	
//	@ManyToOne
//    private OrderItem orderItem;
	
	private boolean inStock=true;
}
