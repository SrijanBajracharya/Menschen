package com.achiever.menschenfahren.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.achiever.menschenfahren.constants.Constants;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.csrf().disable().authorizeRequests().antMatchers(Constants.SERVICE_EVENT_API,
		// "/*").permitAll();
		http.formLogin().disable();
		http.httpBasic().disable();

		http.csrf().disable();

		http.authorizeRequests().antMatchers(Constants.SERVICE_EVENT_API, "/*").permitAll();

	}

}
