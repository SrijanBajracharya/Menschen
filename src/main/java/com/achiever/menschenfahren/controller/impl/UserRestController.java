package com.achiever.menschenfahren.controller.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.base.dto.DataResponse;
import com.achiever.menschenfahren.base.dto.JwtRequest;
import com.achiever.menschenfahren.base.dto.JwtResponse;
import com.achiever.menschenfahren.base.dto.UserCreateDto;
import com.achiever.menschenfahren.base.dto.UserDto;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.controller.UserRestControllerInterface;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.exception.EmailNotFoundException;
import com.achiever.menschenfahren.exception.InvalidUserException;
import com.achiever.menschenfahren.mapper.UserMapper;
import com.achiever.menschenfahren.security.jwt.JwtTokenUtil;
import com.achiever.menschenfahren.service.UserService;

/**
 *
 * @author Srijan Bajracharya
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
public class UserRestController extends BaseController implements UserRestControllerInterface {

    private final UserMapper            userMapper;

    @Autowired
    private UserService                 userService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil          jwtTokenUtil;

    @Autowired
    private PasswordEncoder             bcryptEncoder;

    @Autowired
    public UserRestController(@Nonnull final UserService userService, @Nonnull final AuthenticationManager authenticationManager,
            @Nonnull final JwtTokenUtil jwtTokenUtil) {
        super();
        this.userMapper = new UserMapper();
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Creates a new user.
     */
    @Override
    public ResponseEntity<DataResponse<UserDto>> createUser(@Valid final UserCreateDto request, final boolean alsoVoided) throws InvalidUserException {

        final User user = userMapper.map(request, User.class);
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        final User savedUser = userService.addUser(user);

        final UserDto savedUserDto = userMapper.map(savedUser, UserDto.class);

        if (savedUserDto != null) {
            return buildResponse(savedUserDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Returns all the users based on alsoVoided filter.
     */
    @Override
    public ResponseEntity<DataResponse<List<UserDto>>> getUsers(final boolean alsoVoided) {
        final List<User> users = this.userService.getUsers(alsoVoided);

        final List<UserDto> allUsers = new ArrayList<>();

        for (final User user : users) {
            allUsers.add(this.userMapper.map(user, UserDto.class));
        }

        if (allUsers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return buildResponse(allUsers, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<DataResponse<UserDto>> getUser(final String userId, final boolean alsoVoided) {
        final Optional<User> userOptional = this.userService.findByIdAndVoided(userId, alsoVoided);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            final User user = userOptional.get();
            final UserDto userDto = this.userMapper.map(user, UserDto.class);

            if (userDto != null) {
                return buildResponse(userDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.GONE);
            }
        }
    }

    @Override
    public ResponseEntity<?> createAuthenticationToken(JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final User user = userService.findByEmail(authenticationRequest.getEmail());

        if (user == null) {
            throw new EmailNotFoundException("User email not found.");
        }

        final String token = jwtTokenUtil.generateToken(user);

        return ResponseEntity.ok(new JwtResponse(token));
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
