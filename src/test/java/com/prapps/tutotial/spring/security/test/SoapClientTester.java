package com.prapps.tutotial.spring.security.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import com.prapps.tutorial.spring.ApplicationStarter;

import io.spring.guides.gs_producing_web_service.GetCountryRequest;
import io.spring.guides.gs_producing_web_service.GetCountryResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {ApplicationStarter.class, SoapClientConfig.class})
public class SoapClientTester {

	private String username;
	private String password;
	private SoapClientHeaderInterceptor soapClientHeaderInterceptor;

	@Autowired MockMvc mvc;
	@Autowired WebApplicationContext wac;
	@Autowired WebServiceTemplate webServiceTemplate;

	@Before
	public void setUp() {
		username = "admin";
		password = "admin";
		soapClientHeaderInterceptor = new SoapClientHeaderInterceptor(username, password);
	}

	@Test
	public void shouldAccessSecuredSoapResource() throws Exception {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		webServiceTemplate.setDefaultUri("http://localhost:8080/security-demo/ws");
		GetCountryRequest request = new GetCountryRequest();
		request.setName("Spain");
		webServiceTemplate.setInterceptors(new ClientInterceptor[] {soapClientHeaderInterceptor});
		GetCountryResponse actualResp = (GetCountryResponse) webServiceTemplate
	                .marshalSendAndReceive(request);
		Assert.assertEquals("Spain", actualResp.getCountry().getName());
	}
}
