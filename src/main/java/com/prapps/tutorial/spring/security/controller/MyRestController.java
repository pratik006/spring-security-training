package com.prapps.tutorial.spring.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/secured")
public class MyRestController {

	@RequestMapping(value="/hello", method = {RequestMethod.GET, RequestMethod.POST})
	public String hello() {
		return "hello";
	}
}
