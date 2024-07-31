package com.app.entities;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.app.dto.RestaurantDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data //generates getter,setter,hash code,toString,equals
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) //to generate id automatically
	private Long id;
	
	private String fullName;
	
	private String email;
	
	@JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	private USER_ROLE role=USER_ROLE.ROLE_CUSTOMER;
	
	@JsonIgnore //indicate to ignore this field during seri. & desri.
	
	/*
	 * a field marked as @JsonIgnore can still be persisted 
	 * in a JPA persistence where as a field marked 
	 * @Transient will neither be persisted nor be serialized, de-serialized.
	 */
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy="customer")
	private List<Order> orders=new ArrayList<>();
	
	/*@ElementCollection
	this annotation allows us to store collections of standard library
	 * wrapper types or strings or embeddable objects within
	 * an entity, without having to create separate entities 
	 * and define their relations.*/
	
	@ElementCollection
	private List<Restaurant> favourites=new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true) //so if we delete the user then all its address values also gets deleted automatically
	private List<Address> addresses=new ArrayList<>();
	
	
	
}
