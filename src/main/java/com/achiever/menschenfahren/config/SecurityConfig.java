package com.achiever.menschenfahren.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.security.jwt.JwtAuthenticationEntryPoint;
import com.achiever.menschenfahren.security.jwt.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    // // http.csrf().disable().authorizeRequests().antMatchers(Constants.SERVICE_EVENT_API,
    // // "/*").permitAll();
    // http.formLogin().disable();
    // http.httpBasic().disable();
    //
    // // http.csrf().disable();
    //
    // // http.authorizeRequests().antMatchers(Constants.SERVICE_EVENT_API, "/*").permitAll();
    //
    // // We don't need CSRF for this example
    // http.csrf().disable()
    // // dont authenticate this particular request
    // .authorizeRequests().antMatchers(Constants.SERVICE_EVENT_API + "/authenticate").permitAll().
    // // all other requests need to be authenticated
    // anyRequest().authenticated().and().
    // // make sure we use stateless session; session won't be used to
    // // store user's state.
    // exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
    // .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    //
    // // Add a filter to validate the tokens with every request
    // http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    //
    // }

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService          jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter            jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin().disable();
        http.httpBasic().disable();
        // We don't need CSRF for this example
        http.csrf().disable()
                // dont authenticate this particular request
                .authorizeRequests().antMatchers(Constants.SERVICE_EVENT_API + "/authenticate", Constants.SERVICE_EVENT_API + "/user").permitAll().
                // all other requests need to be authenticated
                anyRequest().authenticated().and().
                // make sure we use stateless session; session won't be used to
                // store user's state.
                exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
