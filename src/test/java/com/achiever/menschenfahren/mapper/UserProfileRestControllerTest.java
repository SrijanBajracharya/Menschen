package com.achiever.menschenfahren.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

import com.achiever.menschenfahren.CustomBooleanStrategy;
import com.achiever.menschenfahren.base.dto.request.UserProfileCreateDto;
import com.achiever.menschenfahren.base.dto.request.UserProfileEditDto;
import com.achiever.menschenfahren.controller.impl.UserProfileRestController;
import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.dao.UserProfileDaoInterface;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;
import com.achiever.menschenfahren.service.UserProfileService;
import com.achiever.menschenfahren.service.impl.AuthenticationService;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserProfileRestControllerTest {

    private static PodamFactory       factory;

    private static final String       userProfileId = "userProfileId";
    private static final String       userId        = "userId";
    private static final String       voidedId      = "voidedId";

    @MockBean
    private UserProfileDaoInterface   userProfileDto;

    @InjectMocks
    private UserProfileRestController restController;

    @SpyBean
    private UserProfileService        userProfileService;

    @MockBean
    private UserDaoInterface          userDao;
    
    @MockBean
    private AuthenticationService authenticationService;

    @BeforeAll
    protected static void initialize() {
        factory = new PodamFactoryImpl();

        factory.getStrategy().addOrReplaceTypeManufacturer(boolean.class, new CustomBooleanStrategy());
        factory.getStrategy().addOrReplaceTypeManufacturer(Boolean.class, new CustomBooleanStrategy());
    }

    @BeforeEach
    void setUp() throws Exception {

        final User user = buildUser();
        user.setId(userId);
        user.setVoided(false);

        final UserProfile userProfile = buildUserProfile();
        userProfile.setUser(user);
        userProfile.setVoided(false);
        userProfile.setId(userProfileId);

        final UserProfile voidedUserProfile = buildUserProfile();
        voidedUserProfile.setUser(user);
        voidedUserProfile.setVoided(true);
        voidedUserProfile.setId(voidedId);

        final List<UserProfile> allUsers = new ArrayList<>();
        allUsers.add(userProfile);
        allUsers.add(voidedUserProfile);

        final List<UserProfile> nonVoidedUsers = new ArrayList<>();
        nonVoidedUsers.add(userProfile);

        Mockito.doReturn(allUsers).when(userProfileDto).findAll();
        Mockito.doReturn(nonVoidedUsers).when(userProfileDto).findByVoided(Mockito.eq(false));

        Mockito.doReturn(Optional.of(user)).when(userDao).findByIdAndVoided(Mockito.eq(userId), Mockito.eq(false));

        Mockito.doAnswer(invocation -> invocation.getArgument(0, UserProfile.class)).when(userProfileDto).save(Mockito.any(UserProfile.class));
        Mockito.doReturn(true).when(userProfileDto).existsById(Mockito.eq(userProfileId));

        Mockito.doReturn(Optional.of(userProfile)).when(userProfileDto).findById(Mockito.eq(userProfileId));
        Mockito.doReturn(Optional.of(voidedUserProfile)).when(userProfileDto).findById(Mockito.eq(voidedId));
    }

    private User buildUser() {
        return factory.manufacturePojo(User.class);
    }

    private UserProfile buildUserProfile() {
        return factory.manufacturePojo(UserProfile.class);
    }

    private UserProfileEditDto buildUserProfileEditDto() {
        return factory.manufacturePojo(UserProfileEditDto.class);
    }

    private UserProfileCreateDto buildCreateDto() {
        return factory.manufacturePojo(UserProfileCreateDto.class);
    }

    // @Test
    public void testGetUsers() throws Exception {
        final var response = restController.getUserProfileByUserId("non-existing", false);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // @Test
    public void testCreateProfile() throws Exception {
        final var response = restController.createProfile(buildCreateDto(), false);

        Mockito.when(authenticationService.getId()).thenReturn("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}
