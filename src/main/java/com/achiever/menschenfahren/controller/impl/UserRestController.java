package com.achiever.menschenfahren.controller.impl;

import java.util.ArrayList;
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
import com.achiever.menschenfahren.entities.response.DataResponse;
import com.achiever.menschenfahren.entities.response.UserCreateDto;
import com.achiever.menschenfahren.entities.response.UserDto;
import com.achiever.menschenfahren.entities.response.UserProfileCreateDto;
import com.achiever.menschenfahren.entities.response.UserProfileDto;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;
import com.achiever.menschenfahren.exception.InvalidUserException;
import com.achiever.menschenfahren.mapper.UserMapper;
import com.achiever.menschenfahren.mapper.UserProfileMapper;
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

	private final UserProfileMapper userProfileMapper;

	@Autowired
	private UserService userService;

	public UserRestController() {
		super();
		this.userMapper = new UserMapper();
		this.userProfileMapper = new UserProfileMapper();
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
	public ResponseEntity<DataResponse<List<UserDto>>> getUsers(final boolean alsoVoided) {
		final List<User> users = this.userService.getUsers(alsoVoided);

		final List<UserDto> allUsers = new ArrayList<>();

		for (final User user : users) {
			allUsers.add(this.userMapper.convertUserToUserDto(user));
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
			final UserDto userDto = this.userMapper.convertUserToUserDto(user);

			if (userDto != null) {
				return buildResponse(userDto, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.GONE);
			}
		}
	}

	@Override
	public ResponseEntity<DataResponse<UserProfileDto>> createProfile(@Nonnull final String userId,
			@Nonnull @Valid final UserProfileCreateDto request, final boolean alsoVoided) throws InvalidUserException {
//		final UserProfile userProfile = this.userProfileMapper.map(request, UserProfile.class);
//		final UserProfile savedUserProfile = this.userService.addProfile(userProfile, alsoVoided);
//
//		final UserProfileDto userProfileDto = this.userProfileMapper.map(savedUserProfile, UserProfileDto.class);

		System.err.println(userId + "userId");
		Optional<User> user = this.userService.findByIdAndVoided(userId, alsoVoided);
		System.err.println("in this position");
		if (user.isPresent()) {
			User savedUser = user.get();
			System.err.println("Inside this position" + savedUser);
			final UserProfile userProfile = this.userProfileMapper.convertUserProfileCreateDtoToUserProfile(request,
					savedUser);
			final UserProfile savedUserProfile = this.userService.addProfile(userProfile, alsoVoided);
			final UserProfileDto userProfileDto = this.userProfileMapper
					.convertUserProfileToUserProfileDto(savedUserProfile);
			if (userProfileDto != null) {
				return buildResponse(userProfileDto, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

	}

}
