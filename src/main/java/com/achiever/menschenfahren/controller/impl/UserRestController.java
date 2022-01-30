package com.achiever.menschenfahren.controller.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.base.controller.UserRestControllerInterface;
import com.achiever.menschenfahren.base.dto.request.FriendsDto;
import com.achiever.menschenfahren.base.dto.request.JwtRequest;
import com.achiever.menschenfahren.base.dto.request.UserCreateDto;
import com.achiever.menschenfahren.base.dto.request.UserEditDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.UserDto;
import com.achiever.menschenfahren.base.exception.InvalidUserException;
import com.achiever.menschenfahren.base.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.mapper.UserMapper;
import com.achiever.menschenfahren.service.impl.AuthenticationService;

/**
 *
 * @author Srijan Bajracharya
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
public class UserRestController extends BaseController implements UserRestControllerInterface {

    private final UserMapper      userMapper;

    @Autowired
    private UserDaoInterface      userDao;

    @Autowired
    private PasswordEncoder       bcryptEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    public UserRestController() {
        super();
        this.userMapper = new UserMapper();
    }

    /**
     * Creates a new user.
     */
    @Override
    public ResponseEntity<DataResponse<UserDto>> createUser(@Valid @Nonnull final UserCreateDto request, final boolean alsoVoided) throws InvalidUserException {

        final User user = userMapper.map(request, User.class);
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        final User savedUser = addUser(user);

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
    public ResponseEntity<DataResponse<List<UserDto>>> getUsers(@Nonnull final boolean alsoVoided) {
        final List<User> users = getUsersBasedOnAlsoVoided(alsoVoided);

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
    public ResponseEntity<DataResponse<UserDto>> getUser(@Nonnull final String userId, final boolean alsoVoided) {
        final Optional<User> userOptional = this.userDao.findByIdAndVoided(userId, alsoVoided);
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
    public ResponseEntity<DataResponse<UserDto>> editUser(@Valid @Nonnull final UserEditDto request, final boolean alsoVoided)
            throws ResourceNotFoundException {
        String userId = authenticationService.getId();

        Optional<User> userOptional = this.userDao.findByIdAndVoided(userId, alsoVoided);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("The user id doesn't exist in our system.");
        } else {
            User user = userOptional.get();
            this.userMapper.map(request, user);
            final User savedUser = this.userDao.save(user);
            return buildResponse(this.userMapper.map(savedUser, UserDto.class), HttpStatus.OK);

        }
    }

    private User addUser(@NonNull final User user) throws InvalidUserException {
        final User userExists = userDao.findByEmail(user.getEmail());
        User savedUser = null;
        if (userExists == null) {
            savedUser = userDao.save(user);
        } else {
            throw new InvalidUserException("Email and/or username must be unique.");
        }
        return savedUser;
    }

    private List<User> getUsersBasedOnAlsoVoided(final boolean alsoVoided) {
        final List<User> users;
        if (alsoVoided) {
            users = userDao.findAll();
        } else {
            users = userDao.findByVoided(alsoVoided);
        }
        return users;
    }

    @Override
    public ResponseEntity<DataResponse<List<FriendsDto>>> getFriendList(@Nonnull final String userId) throws ResourceNotFoundException {
        final Optional<User> userOptional = this.userDao.findById(userId);

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found for an id: " + userId);
        }

        User user = userOptional.get();

        Set<String> friendList = user.getFriends();
        List<User> userFriends = new ArrayList<>();
        if (!CollectionUtils.isEmpty(friendList)) {
            userFriends = userDao.findAllById(friendList);
        }

        List<FriendsDto> result = userMapper.mapAsList(userFriends, FriendsDto.class);

        return buildResponse(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DataResponse<List<FriendsDto>>> getFriendListByToken() throws ResourceNotFoundException {
        String userId = authenticationService.getId();

        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found for an id: " + userId);
        }

        User user = userOptional.get();

        Set<String> friendList = user.getFriends();
        List<User> userFriends = new ArrayList<>();
        if (!CollectionUtils.isEmpty(friendList)) {
            userFriends = userDao.findAllById(friendList);
        }

        List<FriendsDto> result = userMapper.mapAsList(userFriends, FriendsDto.class);

        return buildResponse(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createAuthenticationToken(JwtRequest authenticationRequest) throws Exception {
        return buildResponse(authenticationService.authenticate(authenticationRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DataResponse<UserDto>> getUserByToken(boolean alsoVoided) throws InvalidUserException {
        String userId = authenticationService.getId();
        final Optional<User> userOptional = this.userDao.findByIdAndVoided(userId, alsoVoided);
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
