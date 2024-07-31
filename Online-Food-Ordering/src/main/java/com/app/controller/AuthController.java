package com.app.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.config.JwtProvider;
import com.app.dao.CartDao;
import com.app.dao.UserDao;
import com.app.entities.Cart;
import com.app.entities.USER_ROLE;
import com.app.entities.User;
import com.app.request.LoginRequest;
import com.app.response.AuthResponse;
import com.app.service.CustomerUserDetailsService;

@RestController
@RequestMapping("/auth") //it means whatever end-point we write in this class begins with '/auth'
public class AuthController {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private CustomerUserDetailsService customerUserDetailsService;
	@Autowired
	private CartDao cartDao;
	
	//singup method
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception{
		User isEmailExist=userDao.findByEmail(user.getEmail());
		if(isEmailExist!=null) { //it means the user with given email is already present so we don't allow user with same email to register again
			throw new Exception("Email already in use with another account!!");
		}
		
		User createdUser=new User();
		createdUser.setEmail(user.getEmail());
		createdUser.setFullName(user.getFullName());
		createdUser.setRole(user.getRole());
		createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

		
		User savedUser=userDao.save(createdUser);
		
		//after successfully creating user will create cart for that user
		Cart cart=new Cart();
		cart.setCustomer(savedUser);
		cartDao.save(cart);
		
		Authentication authentication=new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//generating token
		String jwt=jwtProvider.generateToken(authentication);
		AuthResponse authResponse=new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Successfully Registered!!");
		authResponse.setRole(savedUser.getRole());
		
		return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
	}
	
	//Login method
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest req){
		String username=req.getEmail();
		String password=req.getPassword();
		
		Authentication authentication=authenticate(username,password);
		
		//generating token
		Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
		String role=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
		
		String jwt=jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse=new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Successfully Logged-in!!");
		authResponse.setRole(USER_ROLE.valueOf(role));
				
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
			
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails=customerUserDetailsService.loadUserByUsername(username);
		
		if(userDetails==null) { //user with given email is not present in db so user need to register first
			throw new BadCredentialsException("Invalid Username...");
		}
		if(!passwordEncoder.matches(password /*without encoding*/,userDetails.getPassword() /*encoded password in db*/)) {
			throw new BadCredentialsException("Invalid password!!");
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
	}
	
	
	
	
	
}
