package com.prapps.tutorial.spring.rest.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prapps.tutorial.spring.dto.HelloResponse;

@RestController
@RequestMapping("/rest/secured")
public class MyRestController {

	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping(value="/hello", method = {RequestMethod.GET, RequestMethod.POST})
	public HelloResponse hello() {
		return new HelloResponse("hello");
	}

	@RequestMapping(value="/manage", method = {RequestMethod.GET, RequestMethod.POST})
	@Secured("ROLE_ADMIN")
	public HelloResponse manage() {
		return new HelloResponse("manage");
	}
}
