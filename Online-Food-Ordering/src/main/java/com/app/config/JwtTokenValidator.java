package com.app.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtTokenValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// it is for validating jwt token
		String jwt = request.getHeader(JwtConstant.JWT_HEADER);
		// Bearer token
		if (jwt != null) {
			jwt = jwt.substring(7);
			try {
				SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
				String email = String.valueOf(claims.get("email"));
				String authorities = String.valueOf(claims.get("authorities")); // we get all the authorities in string
																				// format like ROLE_CUSTOMER,ROLE_ADMIN

				List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auth);
				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (io.jsonwebtoken.security.SecurityException e) {
				// likely an expired token, handle appropriately
				response.setStatus(HttpStatus.FORBIDDEN.value());
				throw new RuntimeException("Expired or invalid token"); // or a custom exception for expired tokens
			} catch (Exception e) {
				throw new BadCredentialsException("Invalid Token....!!");
			}
		}
		filterChain.doFilter(request, response);
	}

}
