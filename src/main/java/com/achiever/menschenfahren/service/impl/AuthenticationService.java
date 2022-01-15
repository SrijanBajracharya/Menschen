package com.achiever.menschenfahren.service.impl;

import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.achiever.menschenfahren.base.dto.request.JwtRequest;
import com.achiever.menschenfahren.base.dto.response.AuthenticationResponse;
import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.exception.EmailNotFoundException;
import com.achiever.menschenfahren.security.jwt.JwtTokenUtil;
import com.achiever.menschenfahren.service.AuthenticationServiceInterface;

@Service
public class AuthenticationService implements AuthenticationServiceInterface{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserDaoInterface userDao;

	public AuthenticationResponse authenticate(JwtRequest authenticationRequest) throws Exception {
		//
		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

		User user = userDao.findByEmail(authenticationRequest.getEmail());
		
		if (user == null) {
			throw new EmailNotFoundException("User email not found.");
		}
		
		return jwtTokenUtil.generateToken(user);
	}
	
	private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS", e);
        }
    }

}
