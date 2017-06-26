package com.prapps.tutorial.spring.security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationManager implements AuthenticationManager {
	private UserDetailsService daoAuthenticationProvider;
	
	@Autowired 
	public RestAuthenticationManager(AuthenticationManagerBuilder auth) {
		this.daoAuthenticationProvider = auth.getDefaultUserDetailsService();
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String username = String.valueOf(auth.getPrincipal());
		String password = String.valueOf(auth.getCredentials());
		  
		UserDetails user = daoAuthenticationProvider.loadUserByUsername(username);
		if (!user.getPassword().equals(password)) {
		    throw new BadCredentialsException("Bad Credentials");
		}

		return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()) ;
	}
}
