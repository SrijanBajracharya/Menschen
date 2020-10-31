package com.achiever.menschenfahren.controller.impl;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.controller.UserProfileRestControllerInterface;
import com.achiever.menschenfahren.entities.response.DataResponse;
import com.achiever.menschenfahren.entities.response.UserProfileCreateDto;
import com.achiever.menschenfahren.entities.response.UserProfileDto;
import com.achiever.menschenfahren.entities.response.UserProfileEditDto;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;
import com.achiever.menschenfahren.exception.InvalidUserException;
import com.achiever.menschenfahren.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.mapper.UserProfileMapper;
import com.achiever.menschenfahren.service.UserProfileService;
import com.achiever.menschenfahren.service.UserService;

@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
public class UserProfileRestController extends BaseController implements UserProfileRestControllerInterface {

    private final UserProfileMapper userProfileMapper = new UserProfileMapper();

    @Autowired
    private UserProfileService      userProfileService;

    @Autowired
    private UserService             userService;

    @Override
    public ResponseEntity<DataResponse<UserProfileDto>> createProfile(@Nonnull final String userId, @Nonnull @Valid final UserProfileCreateDto request,
            final boolean alsoVoided) throws InvalidUserException {

        final Optional<User> user = this.userService.findByIdAndVoided(userId, alsoVoided);
        if (user.isPresent()) {
            final User savedUser = user.get();
            request.setUserId(savedUser);
            final UserProfile userProfile = this.userProfileMapper.map(request, UserProfile.class);
            final UserProfile savedUserProfile = this.userProfileService.addProfile(userProfile, alsoVoided);
            final UserProfileDto userProfileDto = this.userProfileMapper.map(savedUserProfile, UserProfileDto.class);
            if (userProfileDto != null) {
                return buildResponse(userProfileDto, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @Override
    public ResponseEntity<DataResponse<UserProfileDto>> getUserProfileById(@Nonnull final String id) throws ResourceNotFoundException {
        final UserProfile userProfile = this.findUserProfileById(id);
        final UserProfileDto savedUserProfileDto = this.userProfileMapper.map(userProfile, UserProfileDto.class);
        return buildResponse(savedUserProfileDto, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<DataResponse<UserProfileDto>> editProfile(@Nonnull final String userProfileId, @Valid final UserProfileEditDto request)
            throws ResourceNotFoundException {
        final UserProfile userProfile = findUserProfileById(userProfileId);
        userProfileMapper.map(request, userProfile);

        final UserProfile savedUserProfile = userProfileService.addProfile(userProfile, userProfile.isVoided());
        return buildResponse(userProfileMapper.map(savedUserProfile, UserProfileDto.class), HttpStatus.OK);
    }

    private UserProfile findUserProfileById(@Nonnull final String id) throws ResourceNotFoundException {
        final Optional<UserProfile> userProfileOptional = this.userProfileService.findById(id);
        if (userProfileOptional.isEmpty()) {
            throw new ResourceNotFoundException("No User profile found with id:" + id);
        }
        final UserProfile userProfile = userProfileOptional.get();

        return userProfile;
    }

}
