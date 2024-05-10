package com.app.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class RestaurantDto implements Serializable{
	private String title;
	
	@ElementCollection
	@Column(length=1000,name="image")
    @CollectionTable(name="restaurant_images")
	private List<String> images;
	
	private String description;
	private Long id;
}
