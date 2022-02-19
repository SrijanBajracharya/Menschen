package com.achiever.menschenfahren.controller.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.achiever.menschenfahren.base.controller.UserProfileRestControllerInterface;
import com.achiever.menschenfahren.base.dto.request.UserProfileCreateDto;
import com.achiever.menschenfahren.base.dto.request.UserProfileEditDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.UserDto;
import com.achiever.menschenfahren.base.dto.response.UserProfileDto;
import com.achiever.menschenfahren.base.exception.InvalidUserException;
import com.achiever.menschenfahren.base.exception.MultipleResourceFoundException;
import com.achiever.menschenfahren.base.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.dao.AvatarDaoInterface;
import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.entities.users.Avatar;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;
import com.achiever.menschenfahren.mapper.UserMapper;
import com.achiever.menschenfahren.mapper.UserProfileMapper;
import com.achiever.menschenfahren.service.UserProfileService;
import com.achiever.menschenfahren.service.impl.AuthenticationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
@Slf4j
public class UserProfileRestController extends BaseController implements UserProfileRestControllerInterface {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE);

    private final UserProfileMapper  userProfileMapper     = new UserProfileMapper();

    private final UserMapper         userMapper            = new UserMapper();

    @Autowired
    private UserProfileService       userProfileService;

    @Autowired
    private AvatarDaoInterface       avatarDao;

    @Autowired
    private UserDaoInterface         userDao;

    @Autowired
    private AuthenticationService    authenticationService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<UserProfileDto>> createProfile(@Nonnull @Valid final UserProfileCreateDto request, final boolean alsoVoided)
            throws InvalidUserException {
        System.err.println("I am here");
        String userId = authenticationService.getId();
        System.err.println(userId);
        System.err.println(request.toString());
        final Optional<User> user = this.userDao.findByIdAndVoided(userId, alsoVoided);
        if (user.isPresent()) {
            final User savedUser = user.get();
            request.setUserId(userId);
            System.err.println("before map");
            final UserProfile userProfile = this.userProfileMapper.map(request, UserProfile.class);
            // setting user to userprofile.
            userProfile.setUser(savedUser);
            System.err.println("after map" + userProfile);

            final UserProfile savedUserProfile = this.userProfileService.addProfile(userProfile, alsoVoided);
            System.err.println(savedUserProfile);
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
     *
     * @throws MultipleResourceFoundException
     */
    @Override
    public ResponseEntity<DataResponse<UserProfileDto>> getUserProfileByUserId(@Nonnull final String userId, final boolean alsoVoided)
            throws ResourceNotFoundException, MultipleResourceFoundException {
        final Optional<User> user = this.userDao.findByIdAndVoided(userId, alsoVoided);

        if (user.isPresent()) {
            User savedUser = user.get();
            final UserProfile userProfile = userProfileService.findByUser(savedUser);
            final UserProfileDto savedUserProfileDto = this.userProfileMapper.map(userProfile, UserProfileDto.class);
            savedUserProfileDto.setUserId(savedUser.getId());
            return buildResponse(savedUserProfileDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @Override
    public ResponseEntity<DataResponse<UserProfileDto>> getProfileByToken(final boolean alsoVoided)
            throws ResourceNotFoundException, MultipleResourceFoundException {
        String userId = authenticationService.getId();
        final Optional<User> user = this.userDao.findByIdAndVoided(userId, alsoVoided);

        if (user.isPresent()) {
            User savedUser = user.get();
            final UserProfile userProfile = userProfileService.findByUser(savedUser);
            final UserProfileDto savedUserProfileDto = this.userProfileMapper.map(userProfile, UserProfileDto.class);
            savedUserProfileDto.setUserId(savedUser.getId());
            return buildResponse(savedUserProfileDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * {@inheritDoc}
     *
     * @throws MultipleResourceFoundException
     */
    @Override
    public ResponseEntity<DataResponse<UserProfileDto>> editProfile(@Valid final UserProfileEditDto request)
            throws ResourceNotFoundException, MultipleResourceFoundException {
        String userId = authenticationService.getId();
        final Optional<User> user = this.userDao.findById(userId);
        if (user.isPresent()) {
            User savedUser = user.get();
            UserProfile userProfile;
            userProfile = userProfileService.findByUser(savedUser);
            userProfileMapper.map(request, userProfile);
            final UserProfile savedUserProfile = userProfileService.addProfile(userProfile, userProfile.isVoided());
            return buildResponse(userProfileMapper.map(savedUserProfile, UserProfileDto.class), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> updateUserPicture(@Nonnull final MultipartFile avatar) {
        String userId = authenticationService.getId();

        final var contentType = avatar.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().body("Invalid image type. Only allowed: " + ALLOWED_CONTENT_TYPES);
        }

        try {

            final Optional<Avatar> savedAvatarOptional = this.avatarDao.findByUserId(userId);

            if (savedAvatarOptional.isPresent() && null != savedAvatarOptional.get().getAvatar()) {
                Avatar savedAvatar = savedAvatarOptional.get();
                savedAvatar.setAvatar(avatar.getBytes());
                savedAvatar.setAvatarType(contentType);
                savedAvatar.setModifiedTimestamp(new Date());
                this.avatarDao.save(savedAvatar);
                log.info("Updated avatar for account {}.", userId);
            } else {
                final var avatarBytes = avatar.getBytes();
                final var avatarData = new Avatar();
                avatarData.setAvatar(avatarBytes);
                avatarData.setAvatarType(contentType);
                avatarData.setUserId(userId);

                this.avatarDao.save(avatarData);
                log.info("Saved avatar for account {}.", userId);
            }

        } catch (final IOException e) {
            return ResponseEntity.badRequest().body("Could not update avatar: " + e.getMessage());
        }

        return ResponseEntity.ok("Updated your avatar.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<byte[]> getAvatar() {
        String userId = authenticationService.getId();
        final Optional<Avatar> avatar = this.avatarDao.findByUserId(userId);
        if (avatar.isPresent() && null != avatar.get().getAvatar()) {
            final var mediaType = avatar.get().getAvatarType();
            MediaType responseMediaType;
            try {
                responseMediaType = MediaType.valueOf(mediaType);
            } catch (final InvalidMediaTypeException e) {
                log.warn("Invalid media type.{}", mediaType);
                responseMediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return ResponseEntity.ok().contentType(responseMediaType).body(avatar.get().getAvatar());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
