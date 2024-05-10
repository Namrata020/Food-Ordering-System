package com.app.config;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

/*
 *Imagine you're setting up your kitchen. 
 *You have a blueprint that outlines where each appliance and utensil should go. 
 *Similarly, in Spring Boot, a class annotated with '@Configuration' acts as a blueprint for your 
 *application's components (beans). 
 */

@Configuration
@EnableWebSecurity
public class AppConfig {
//inside this class will configure our spring security
	
	@Bean //'@Bean' in Spring Boot allows you to define and configure custom components or objects, much like adding or customizing appliances in your kitchen to suit your needs
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(Authorize -> Authorize
					.antMatchers("/api/admin/**").hasAnyRole("RESTAURANT_OWNER","ADMIN") //this end-point for user with role either as restaurant owner or admin
					.antMatchers("/api/**").authenticated() //if end-point starts with this then make it secure
					.anyRequest().permitAll()
			).addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
			 .csrf(csrf -> csrf.disable())
			 .cors(cors -> cors.configurationSource(corsConfigurationSource()));
		
			//inside authorizeHttpRequests() will define which end-points should be whitelisted or which end-points are accessible by particular role user
			
		return http.build();
	}

	private CorsConfigurationSource corsConfigurationSource() {
		
		return new CorsConfigurationSource() {

			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				// TODO Auto-generated method stub
					CorsConfiguration cfg=new CorsConfiguration();
					cfg.setAllowedOrigins(Arrays.asList(
							"https://food.vercel.app/",
							"http://localhost:3000"
					)); //inside this list we need to provide all the frontend url from which you want to access this backend
					cfg.setAllowedMethods(Collections.singletonList("*"));
					cfg.setAllowCredentials(true);
					cfg.setAllowedHeaders(Collections.singletonList("*"));
					cfg.setExposedHeaders(Arrays.asList("Authorization"));
					cfg.setMaxAge(3600L);
					
			return cfg;
			}	
		};
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); //will BCrypt the password and then store it in our db for security
	}
	
	
	
	

	
	
	
	
	
	
}
