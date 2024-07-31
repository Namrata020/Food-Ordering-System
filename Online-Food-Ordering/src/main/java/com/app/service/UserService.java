package com.app.service;

import com.app.entities.User;

public interface UserService {

	//method to find user by jwt token
	public User findUserByJwtToken(String jwt) throws Exception;
	//method to find user by email
	public User findUserByEmail(String email) throws Exception;
	
	
}
