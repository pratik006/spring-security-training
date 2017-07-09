package com.prapps.tutorial.spring.rest.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/error")
public class MyCustomErrorController implements ErrorController {

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public void error(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @RequestMapping("/unauthorised")
    @ResponseBody
    public String unAuthorised(HttpServletRequest request) throws Exception {
        return "Unauthorised Request";
	}

	@Override
	public String getErrorPath() {
		return "error";
	}

}
