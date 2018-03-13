# spring-security-training

Purpose:
1. Configure Spring-Security for Web Authentication
2. Configure Spring-Security for REST authentication
3. Configure Spring-Security for SOAP authentication

Web Service WSDL location: http://localhost:8080/security-demo/ws/countries.wsdl



# Steps to enable Spring Security
1. Uncomment the maven dependencies in pom.xml
		<!-- Spring Security dependencies -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.security</groupId>
		    <artifactId>spring-security-test</artifactId>
		    <scope>test</scope>
		</dependency>

2. Uncomment the annotation in ApplicationStarter class along with the import statement
	@EnableGlobalMethodSecurity(securedEnabled = true) 
	
3. Uncomment the @Secured annotation in MyRestController class along with the import statement

4. Move the security folder from the project-root folder into com/prapps/tutorial/spring -- it has all the security configurations

5. Similarly move the test folder from project-root folder into src/ ---- it has all the unit testcases 

## For enabling SOAP security
1. Uncomment the @Autowired SoapAuthenticationInterceptor soapAuthenticationInterceptor; in WebServiceConfig
2. Add this interceptor in public void addInterceptors(List<EndpointInterceptor> interceptors)
3. uncomment/add the required import statements

