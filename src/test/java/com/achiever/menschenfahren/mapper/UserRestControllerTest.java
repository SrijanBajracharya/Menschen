package com.achiever.menschenfahren.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

import com.achiever.menschenfahren.CustomBooleanStrategy;
import com.achiever.menschenfahren.controller.impl.UserProfileRestController;
import com.achiever.menschenfahren.controller.impl.UserRestController;
import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.dao.UserProfileDaoInterface;
import com.achiever.menschenfahren.entities.response.UserCreateDto;
import com.achiever.menschenfahren.entities.response.UserEditDto;
import com.achiever.menschenfahren.entities.response.UserProfileCreateDto;
import com.achiever.menschenfahren.entities.response.UserProfileEditDto;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;
import com.achiever.menschenfahren.service.UserProfileService;
import com.achiever.menschenfahren.service.UserService;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserRestControllerTest {

    private static PodamFactory       factory;

    private static final String       userId        = "userId";
    private static final String       voidedId      = "voidedId";

    @MockBean
    private UserDaoInterface          userDao;

    @InjectMocks
    private UserRestController        restController;

    @SpyBean
    private UserService               userService;

    private static final String       userProfileId = "userProfileId";

    @MockBean
    private UserProfileDaoInterface   userProfileDto;

    @InjectMocks
    private UserProfileRestController restProfileController;

    @SpyBean
    private UserProfileService        userProfileService;

    @BeforeAll
    protected static void initialize() {
        factory = new PodamFactoryImpl();

        factory.getStrategy().addOrReplaceTypeManufacturer(boolean.class, new CustomBooleanStrategy());
        factory.getStrategy().addOrReplaceTypeManufacturer(Boolean.class, new CustomBooleanStrategy());
    }

    @BeforeEach
    void setUp() throws Exception {
        final User user = buildUser();
        user.setVoided(false);
        user.setId(userId);

        final User voidedUser = buildUser();
        voidedUser.setVoided(true);
        voidedUser.setId(voidedId);

        final List<User> allUsers = new ArrayList<>();
        allUsers.add(user);
        allUsers.add(voidedUser);

        final List<User> nonVoidedUsers = new ArrayList<>();
        nonVoidedUsers.add(user);

        Mockito.doReturn(allUsers).when(userDao).findAll();
        Mockito.doReturn(nonVoidedUsers).when(userDao).findByVoided(Mockito.eq(false));

        Mockito.doAnswer(invocation -> invocation.getArgument(0, User.class)).when(userDao).save(Mockito.any(User.class));
        Mockito.doReturn(true).when(userDao).existsById(Mockito.eq(userId));

        Mockito.doReturn(Optional.of(user)).when(userDao).findByIdAndVoided(Mockito.eq(userId), Mockito.eq(false));

        Mockito.doReturn(Optional.of(user)).when(userDao).findById(Mockito.eq(userId));
        Mockito.doReturn(Optional.of(voidedUser)).when(userDao).findById(Mockito.eq(voidedId));

        final UserProfile userProfile = buildUserProfile();
        userProfile.setUser(user);
        userProfile.setVoided(false);
        userProfile.setId(userProfileId);

        final UserProfile voidedUserProfile = buildUserProfile();
        voidedUserProfile.setUser(user);
        voidedUserProfile.setVoided(true);
        voidedUserProfile.setId(voidedId);

        final List<UserProfile> allProfileUsers = new ArrayList<>();
        allProfileUsers.add(userProfile);
        allProfileUsers.add(voidedUserProfile);

        final List<UserProfile> nonVoidedProfileUsers = new ArrayList<>();
        nonVoidedProfileUsers.add(userProfile);

        Mockito.doReturn(allUsers).when(userProfileDto).findAll();
        Mockito.doReturn(nonVoidedUsers).when(userProfileDto).findByVoided(Mockito.eq(false));

        Mockito.doAnswer(invocation -> invocation.getArgument(0, UserProfile.class)).when(userProfileDto).save(Mockito.any(UserProfile.class));
        Mockito.doReturn(true).when(userProfileDto).existsById(Mockito.eq(userProfileId));

        Mockito.doReturn(Optional.of(userProfile)).when(userProfileDto).findById(Mockito.eq(userProfileId));
        Mockito.doReturn(Optional.of(voidedUserProfile)).when(userProfileDto).findById(Mockito.eq(voidedId));
    }

    private User buildUser() {
        return factory.manufacturePojo(User.class);
    }

    private UserEditDto buildUserEditDto() {
        return factory.manufacturePojo(UserEditDto.class);
    }

    private UserCreateDto buildCreateDto() {
        return factory.manufacturePojo(UserCreateDto.class);
    }

    private UserProfile buildUserProfile() {
        return factory.manufacturePojo(UserProfile.class);
    }

    private UserProfileEditDto buildUserProfileEditDto() {
        return factory.manufacturePojo(UserProfileEditDto.class);
    }

    private UserProfileCreateDto buildCreateProfileDto() {
        return factory.manufacturePojo(UserProfileCreateDto.class);
    }

    @Test
    public void testGetUserProfile() throws Exception {
        final var response = restProfileController.getUserProfileById(userProfileId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // @Test
    public void testCreateProfile() throws Exception {
        final var response = restProfileController.createProfile(userId, buildCreateProfileDto(), false);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetUsers() throws Exception {
        var response = restController.getUsers(false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        response = restController.getUsers(true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetUser() throws Exception {
        var response = restController.getUser("non-existing", false);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        response = restController.getUser(userId, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getData().getId());
    }

    @Test
    public void testCreateUser() throws Exception {
        final UserCreateDto request = buildCreateDto();

        final var response = restController.createUser(request, false);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
