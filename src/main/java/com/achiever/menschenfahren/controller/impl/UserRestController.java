package com.achiever.menschenfahren.controller.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.base.dto.request.UserCreateDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.UserDto;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.controller.UserRestControllerInterface;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.exception.InvalidUserException;
import com.achiever.menschenfahren.mapper.UserMapper;
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
    private UserService      userService;

    public UserRestController() {
        super();
        this.userMapper = new UserMapper();
    }

    /**
     * Creates a new user.
     */
    @Override
    public ResponseEntity<DataResponse<UserDto>> createUser(@Valid final UserCreateDto request, final boolean alsoVoided) throws InvalidUserException {

        final User user = userMapper.map(request, User.class);
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

}
