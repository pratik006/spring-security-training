package com.prapps.tutorial.spring.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.prapps.tutorial.spring.security.rest.JwtTokenProcessingFilter;
import com.prapps.tutorial.spring.security.rest.RestAuthenticationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Configuration
    @Order(1)
    public static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
		
		public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/rest/secured/**";
		
		@Autowired AccessDeniedHandler accessDeniedHandler;
		@Autowired @Qualifier("webAuthenticationSuccessHandler") AuthenticationSuccessHandler webAuthenticationSuccessHandler;
		@Autowired RestAuthenticationManager restAuthenticationManager;
		@Autowired private JwtTokenProcessingFilter jwtTokenProcessingFilter;
		
		@Override
		protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()
				.withUser("admin").password("admin").roles("ADMIN")
				.and().withUser("user").password("user").roles("USER");
			restAuthenticationManager.setRestAuthenticationManager(auth);
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.csrf().ignoringAntMatchers("/rest/**").and().authorizeRequests()
				.antMatchers("/login.html").permitAll()
				.antMatchers("/index.html").hasAnyRole("USER", "ADMIN")
				.antMatchers("/manage").hasAnyRole("ADMIN")
				.and()
					.formLogin()
						.loginPage("/login.html").loginProcessingUrl("/login")
						.successHandler(webAuthenticationSuccessHandler)
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/index.html").invalidateHttpSession(true)
				.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
				.and().csrf().and()
				.authorizeRequests()
	                .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected API End-points
	                	.and()
	                		.addFilterBefore(jwtTokenProcessingFilter, UsernamePasswordAuthenticationFilter.class);;
		};
    }
}
