package com.achiever.menschenfahren.controller.impl;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.base.dto.DataResponse;
import com.achiever.menschenfahren.base.dto.UserDto;
import com.achiever.menschenfahren.base.dto.UserProfileCreateDto;
import com.achiever.menschenfahren.base.dto.UserProfileDto;
import com.achiever.menschenfahren.base.dto.UserProfileEditDto;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.controller.UserProfileRestControllerInterface;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;
import com.achiever.menschenfahren.exception.InvalidUserException;
import com.achiever.menschenfahren.exception.MultipleResourceFoundException;
import com.achiever.menschenfahren.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.mapper.UserMapper;
import com.achiever.menschenfahren.mapper.UserProfileMapper;
import com.achiever.menschenfahren.service.UserProfileService;
import com.achiever.menschenfahren.service.UserService;

@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
public class UserProfileRestController extends BaseController implements UserProfileRestControllerInterface {

    private final UserProfileMapper userProfileMapper = new UserProfileMapper();

    private final UserMapper        userMapper        = new UserMapper();

    @Autowired
    private UserProfileService      userProfileService;

    @Autowired
    private UserService             userService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<UserProfileDto>> createProfile(@Nonnull final String userId, @Nonnull @Valid final UserProfileCreateDto request,
            final boolean alsoVoided) throws InvalidUserException {

        final Optional<User> user = this.userService.findByIdAndVoided(userId, alsoVoided);
        if (user.isPresent()) {
            final User savedUser = user.get();
            request.setUserId(userId);
            final UserProfile userProfile = this.userProfileMapper.map(request, UserProfile.class);
            // setting user to userprofile.
            userProfile.setUser(savedUser);

            final UserProfile savedUserProfile = this.userProfileService.addProfile(userProfile, alsoVoided);
            final UserProfileDto userProfileDto = this.userProfileMapper.map(savedUserProfile, UserProfileDto.class);
            if (userProfileDto != null) {
                userProfileDto.setUserId(savedUser.getId());
                UserDto userDto = this.userMapper.map(savedUser, UserDto.class);
                userProfileDto.setUser(userDto);
            }

            if (userProfileDto != null) {
                return buildResponse(userProfileDto, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<UserProfileDto>> getUserProfileById(@Nonnull final String id) throws ResourceNotFoundException {
        final UserProfile userProfile = this.findUserProfileById(id);
        final UserProfileDto savedUserProfileDto = this.userProfileMapper.map(userProfile, UserProfileDto.class);
        return buildResponse(savedUserProfileDto, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<DataResponse<UserProfileDto>> getUserProfileByUserId(@Nonnull final String userId, final boolean alsoVoided)
            throws ResourceNotFoundException, MultipleResourceFoundException {
        final Optional<User> user = this.userService.findByIdAndVoided(userId, alsoVoided);

        if (user.isPresent()) {
            User savedUser = user.get();
            final UserProfile userProfile = this.findUserProfileByUser(savedUser);
            final UserProfileDto savedUserProfileDto = this.userProfileMapper.map(userProfile, UserProfileDto.class);
            savedUserProfileDto.setUserId(savedUser.getId());
            return buildResponse(savedUserProfileDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<UserProfileDto>> editProfile(@Nonnull final String userProfileId, @Valid final UserProfileEditDto request)
            throws ResourceNotFoundException {
        final UserProfile userProfile = findUserProfileById(userProfileId);
        userProfileMapper.map(request, userProfile);

        final UserProfile savedUserProfile = userProfileService.addProfile(userProfile, userProfile.isVoided());
        return buildResponse(userProfileMapper.map(savedUserProfile, UserProfileDto.class), HttpStatus.OK);
    }

    /**
     * Find User profile by the id.
     *
     * @param id
     *            The identifier of user profile.
     * @return User profile
     * @throws ResourceNotFoundException
     *             Thrown if the resource is not found.
     */
    private UserProfile findUserProfileById(@Nonnull final String id) throws ResourceNotFoundException {
        final Optional<UserProfile> userProfileOptional = this.userProfileService.findById(id);
        if (userProfileOptional.isEmpty()) {
            throw new ResourceNotFoundException("No User profile found with id:" + id);
        }
        final UserProfile userProfile = userProfileOptional.get();

        return userProfile;
    }

    private UserProfile findUserProfileByUser(@Nonnull final User user) throws ResourceNotFoundException, MultipleResourceFoundException {
        final UserProfile userProfile = this.userProfileService.findByUser(user);
        return userProfile;
    }

}
