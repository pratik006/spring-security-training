package com.prapps.tutorial.spring.security.filter;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Node;

import com.prapps.tutorial.spring.security.jwt.JwtTokenHelper;

@Component
public class SoapAuthenticationInterceptor implements EndpointInterceptor {

	@Override
	public boolean handleRequest(MessageContext ctx, Object arg1) throws Exception {
		SoapMessage soapMessage  = (SoapMessage) ctx.getRequest();
		SoapHeader soapHeader = soapMessage.getSoapHeader();
		Source bodySource = soapHeader .getSource();
		DOMSource bodyDomSource = (DOMSource) bodySource;
		Node bodyNode = bodyDomSource.getNode();
		if (bodyNode.getFirstChild() == null) {
			throw new AuthenticationCredentialsNotFoundException("No JWT token found in request headers");
		}

		String token = bodyNode.getTextContent();
		if (token == null) {
			//throw new JwtTokenMissingException("No JWT token found in request headers");
			//throw new RuntimeException("No JWT token found in request headers");
			throw new AuthenticationCredentialsNotFoundException("No JWT token found in request headers");
		}
		//String authToken = token.substring(7);
		UserDetails user = JwtTokenHelper.verifyToken(token);

		return user != null;
	}

	@Override public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception { return false; }

	@Override public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception { return false; }

	@Override public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception { }

}
