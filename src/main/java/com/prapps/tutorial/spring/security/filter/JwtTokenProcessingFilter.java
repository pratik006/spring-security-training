package com.prapps.tutorial.spring.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.prapps.tutorial.spring.rest.security.TokenBasedAuthenticationSuccessHandlerImpl;
import com.prapps.tutorial.spring.security.exception.SecurityException;
import com.prapps.tutorial.spring.security.jwt.JwtTokenHelper;

public class JwtTokenProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private final String TOKEN_FILTER_APPLIED = "TOKEN_FILTER_APPLIED";

	@Autowired
	public JwtTokenProcessingFilter(AuthenticationManager authenticationManager,
			AuthenticationFailureHandler authenticationFailureHandler, String url) {
		super(url);
		super.setAuthenticationManager(authenticationManager);
		setAuthenticationSuccessHandler(new TokenBasedAuthenticationSuccessHandlerImpl());
		setAuthenticationFailureHandler(authenticationFailureHandler);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		request.setAttribute(TOKEN_FILTER_APPLIED, Boolean.TRUE);
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			//throw new JwtTokenMissingException("No JWT token found in request headers");
			//throw new RuntimeException("No JWT token found in request headers");
			throw new AuthenticationCredentialsNotFoundException("No JWT token found in request headers");
		}

		String authToken = header.substring(7);
		UserDetails userDetails;
		try {
			userDetails = JwtTokenHelper.verifyToken(authToken);
		} catch (SecurityException e) {
			throw new AuthenticationCredentialsNotFoundException(e.getMsg());
		}
		return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		if (request.getAttribute(TOKEN_FILTER_APPLIED) != null) {
			arg2.doFilter(request, response);
		} else {
			super.doFilter(arg0, arg1, arg2);
		}
	}
}
