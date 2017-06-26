package com.prapps.tutorial.spring.security.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		//super.onAuthenticationFailure(request, response, exception);
		if ("application/json".equals(request.getHeader("Accept"))) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		} else {
			response.sendRedirect("/error");
		}
	}
}
