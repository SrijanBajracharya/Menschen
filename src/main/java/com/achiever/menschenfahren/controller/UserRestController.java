package com.achiever.menschenfahren.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.entities.response.DataResponse;
import com.achiever.menschenfahren.entities.response.UserCreateDto;
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
@RequestMapping("/api")
public class UserRestController extends BaseController implements UserRestControllerInterface {

	// private final UserMapper userMapper = new UserMapper();

	@Autowired
	private UserService userService;

	@Override
	public ResponseEntity<DataResponse<User>> createUser(@Valid final UserCreateDto request) {
//		final User user = this.userMapper.map(request, User.class);
//		final User savedUser = this.userService.addUser(user);
//
//		if (savedUser != null) {
//			return buildResponse(savedUser, HttpStatus.CREATED);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		}
		return null;
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
