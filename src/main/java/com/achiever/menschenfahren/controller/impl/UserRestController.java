package com.achiever.menschenfahren.controller.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.controller.UserRestControllerInterface;
import com.achiever.menschenfahren.controller.mapper.UserMapper;
import com.achiever.menschenfahren.entities.response.DataResponse;
import com.achiever.menschenfahren.entities.response.UserCreateDto;
import com.achiever.menschenfahren.entities.response.UserDto;
import com.achiever.menschenfahren.entities.response.UserProfileEditDto;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;
import com.achiever.menschenfahren.exception.InvalidUserException;
import com.achiever.menschenfahren.service.UserService;

/**
 *
 * @author Srijan Bajracharya
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
public class UserRestController extends BaseController implements UserRestControllerInterface {

	private final UserMapper userMapper;
	@Autowired
	private UserService userService;

	public UserRestController() {
		super();
		userMapper = new UserMapper();
	}

	@Override
	public ResponseEntity<DataResponse<UserDto>> createUser(@Valid final UserCreateDto request,
			final boolean alsoVoided) {

		System.err.println("inside createUser request" + request);

		// final User user = userMapper.map(request, User.class);
		// System.err.println(user + "####user");
		// final User savedUser = userService.addUser(user);

		// final UserDto savedUserDto = userMapper.map(savedUser, UserDto.class);
		// System.err.println(savedUserDto + "####userDto saved");

		final User user = this.userMapper.convertUserCreateDtoToUser(request);
		final User savedUser = this.userService.addUser(user);

		final UserDto savedUserDto = this.userMapper.convertUserToUserDto(savedUser);
		if (savedUserDto != null) {
			return buildResponse(savedUserDto, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@Override
	public ResponseEntity<DataResponse<List<User>>> getUsers(final boolean alsoVoided) {
		final List<User> users = this.userService.getUsers(alsoVoided);

		if (users.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return buildResponse(users, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<DataResponse<User>> getUser(final String userId, final boolean alsoVoided) {
		final Optional<User> userOptional = this.userService.getUser(userId, alsoVoided);
		if (!userOptional.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			final User user = userOptional.get();
			if (!alsoVoided && user.isVoided()) {
				return new ResponseEntity<>(HttpStatus.GONE);
			} else {
				return buildResponse(user, HttpStatus.OK);
			}
		}
	}

	@Override
	public ResponseEntity<DataResponse<UserProfile>> createProfile(@Nonnull final String userId,
			@Nonnull @Valid final UserProfileEditDto request, final boolean alsoVoided) throws InvalidUserException {
//		final UserProfile userProfile = this.userMapper.map(request, UserProfile.class);
//		final UserProfile savedUserProfile = this.userService.addProfile(userProfile, alsoVoided);
//
//		if (savedUserProfile != null) {
//			return buildResponse(savedUserProfile, HttpStatus.CREATED);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		}
		return null;
	}

}
