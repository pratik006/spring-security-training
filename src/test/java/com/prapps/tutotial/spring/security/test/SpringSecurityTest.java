package com.prapps.tutotial.spring.security.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prapps.tutorial.spring.ApplicationStarter;
import com.prapps.tutorial.spring.security.dto.HelloResponse;
import com.prapps.tutorial.spring.security.jwt.JwtTokenHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {ApplicationStarter.class})
public class SpringSecurityTest {
	private static final Logger LOG = Logger.getLogger(SpringSecurityTest.class);

	private String username;
	private String password;
	private GrantedAuthority auth;

	@Autowired MockMvc mvc;
	@Autowired WebApplicationContext wac;

	@Before
	public void setUp() {
		username = "admin";
		password = "admin";
		auth = new GrantedAuthority() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getAuthority() { return "ROLE_ADMIN"; }
		};
	}

	@Test
	public void shouldGetToken() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/rest/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\": \""+username+"\", \"password\": \""+password+"\"}")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					String token = result.getResponse().getHeader("x-authtoken");
					String contentType = result.getResponse().getHeader("Content-Type");
					String content = new String(result.getResponse().getContentAsByteArray());

					LOG.debug("contentType: "+contentType+"\tcontent: " + content);
					LOG.debug("token: " + token);
					UserDetails userDetials = JwtTokenHelper.verifyToken(token);
					LOG.debug("username: " + userDetials.getUsername());
  					Assert.assertEquals(username, userDetials.getUsername());
  					Assert.assertEquals(auth.getAuthority(), userDetials.getAuthorities().iterator().next().getAuthority());
				}
			});
	}

	@Test
	public void shouldFailWithUnauthException() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/rest/secured/hello")
			.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}

	@Test
	public void shouldAccessSecuredResource() throws Exception {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		String token = JwtTokenHelper.createJsonWebToken(new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(auth)));
		MvcResult mvcResult =  mvc.perform(MockMvcRequestBuilders.post("/rest/secured/hello")
			.header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andReturn();
		ObjectMapper mapper = new ObjectMapper();
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		LOG.debug("Response: " + jsonResponse);
		HelloResponse actualResp = mapper.readValue(jsonResponse, HelloResponse.class);
		Assert.assertEquals("hello", actualResp.getMessage());
	}

	@Test
	public void shouldAccessSecuredSoapResource() throws Exception {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		String token = JwtTokenHelper.createJsonWebToken(new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(auth)));
		MvcResult mvcResult =  mvc.perform(MockMvcRequestBuilders.post("/ws")
			.header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andReturn();
		ObjectMapper mapper = new ObjectMapper();
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		LOG.debug("Response: " + jsonResponse);
		HelloResponse actualResp = mapper.readValue(jsonResponse, HelloResponse.class);
		Assert.assertEquals("hello", actualResp.getMessage());
	}
}
