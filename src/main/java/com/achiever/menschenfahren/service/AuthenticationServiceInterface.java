package com.achiever.menschenfahren.service;

import com.achiever.menschenfahren.base.dto.request.JwtRequest;
import com.achiever.menschenfahren.base.dto.response.AuthenticationResponse;

public interface AuthenticationServiceInterface {
	
	/**
     * Returns authenticate users response
     *
     * @param authenticationRequest
     *            contains the credentials for the user to be authenticated
     * @return authenticated user's response
     */
	public AuthenticationResponse authenticate(JwtRequest authenticationRequest) throws Exception;
}
