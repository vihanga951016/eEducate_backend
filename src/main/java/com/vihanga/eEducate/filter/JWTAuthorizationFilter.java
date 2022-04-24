package com.vihanga.eEducate.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vihanga.eEducate.constant.SecurityConstant;
import com.vihanga.eEducate.utility.JWTTokenPorvider;

//Check the user has bad token
//If something happen in this class it's happens only once 
//because of this 'OncePerRequestFilter' class.
@Component //Now we can have bean available whenever starts this application.
public class JWTAuthorizationFilter extends OncePerRequestFilter{

	private JWTTokenPorvider jwtTokenPorvider;
	
	public JWTAuthorizationFilter(JWTTokenPorvider jwtTokenPorvider) {

		this.jwtTokenPorvider = jwtTokenPorvider;
	}

	// If token is valid, user is valid ,once verify that information, we can set that user for the authenticated user. 
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		//Check the request if it is option(To collect information about the server)
		if(request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)) {
			//If it is an option, send it through the server.
			response.setStatus(HttpStatus.OK.value());
		} else {
			
			//Get the actual token from header from request.
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			 
			   //Authentication header is null or Authentication header doesn't start from 'Bearer' word.
			if(authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstant.TOKEN_PREFIX)) { 
				
				filterChain.doFilter(request, response);//Doesn't do anything(We just let the continue normal flow of request).
				return;
				
			}		
			
			//Remove the Bearer(TOKEN_PREFIX) from token.
			String token = authorizationHeader.substring(SecurityConstant.TOKEN_PREFIX.length());
			
			//Get the username.
			String username = jwtTokenPorvider.getSubject(token);
			System.err.println(username + jwtTokenPorvider.isTokenValid(username, token));
			//Now check the token and username is valid and there is no user details set to security context header.
			if(jwtTokenPorvider.isTokenValid(username, token) && SecurityContextHolder.getContext().getAuthentication() == null) {

				System.err.println("is valid");
				List<GrantedAuthority> authorities = jwtTokenPorvider.getAuthorities(token); //Get all Authorities.
				Authentication authentication = jwtTokenPorvider.getAuthentication(username, authorities, request); //Get Authentications.
				SecurityContextHolder.getContext().setAuthentication(authentication);//Set user authentications to the security context.
			} else {
				System.err.println("is clearContext");
				SecurityContextHolder.clearContext(); //User is clear to system.
			}
		}
		
		filterChain.doFilter(request, response);
		
	}
}
