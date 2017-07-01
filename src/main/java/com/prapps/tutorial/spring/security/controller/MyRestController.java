package com.prapps.tutorial.spring.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prapps.tutorial.spring.security.dto.HelloResponse;

@RestController
@RequestMapping("/rest/secured")
public class MyRestController {

	@RequestMapping(value="/hello", method = {RequestMethod.GET, RequestMethod.POST})
	public HelloResponse hello() {
		return new HelloResponse("hello");
	}
}
