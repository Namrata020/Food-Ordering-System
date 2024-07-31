package com.app.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@OneToOne
	private User owner;
	
	private String name;
	private String description;
	private String cuisineType;
	
	@OneToOne
	private Address address;
	
	@Embedded
	private ContactInformation contactInformation;
	
	private String openingHours;
	
	@OneToMany
	private List<Order> orders=new ArrayList<>();
	
	@ElementCollection
	@Column(length=1000)
	private List<String> images;
	
	private LocalDateTime registrationDate;
	private boolean open;
	
	@JsonIgnore //will create separate API to fetch food items of a restaurant
	@OneToMany(mappedBy="restaurant",cascade=CascadeType.ALL) //when restaurant is deleted all its corresponding food items will be deleted
	private List<Food> foods=new ArrayList<>();
}
