package com.achiever.menschenfahren.config;

import java.lang.reflect.AccessibleObject;
import java.security.AccessController;
import java.security.PrivilegedAction;

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

	static void setAccessible(final AccessibleObject ao, final boolean accessible) {
		if (System.getSecurityManager() == null) {
			ao.setAccessible(accessible); // <~ Dragons
		} else {
			AccessController.doPrivileged((PrivilegedAction) () -> {
				ao.setAccessible(accessible); // <~ moar Dragons
				return null;
			});
		}
	}
}
