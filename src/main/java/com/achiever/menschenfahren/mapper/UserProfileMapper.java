package com.achiever.menschenfahren.mapper;

import javax.annotation.Nonnull;

import com.achiever.menschenfahren.entities.response.UserProfileCreateDto;
import com.achiever.menschenfahren.entities.response.UserProfileDto;
import com.achiever.menschenfahren.entities.response.UserProfileEditDto;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class UserProfileMapper extends ConfigurableMapper {

	@Override
	public void configure(final MapperFactory factory) {

		System.err.println("insidee mapperfactory");
		factory.classMap(UserProfile.class, UserProfileDto.class).byDefault().register();
		factory.classMap(UserProfile.class, UserProfileCreateDto.class).byDefault().register();
		factory.classMap(UserProfile.class, UserProfileEditDto.class).mapNullsInReverse(false).byDefault().register();
	}

	public UserProfile convertUserProfileCreateDtoToUserProfile(@Nonnull final UserProfileCreateDto request,
			@Nonnull User user) {
		UserProfile userProfile = new UserProfile();
		userProfile.setAddress(request.getAddress());
		userProfile.setDateOfBirth(request.getDateOfBirth());
		userProfile.setDescription(request.getDescription());
		userProfile.setEducation(request.getEducation());
		userProfile.setExperiences(request.getExperiences());
		userProfile.setGender(request.getGender());
		userProfile.setHobbies(request.getHobbies());
		userProfile.setPhoneNumber(request.getPhoneNumber());
		userProfile.setUserId(user);
		return userProfile;
	}

	public UserProfileDto convertUserProfileToUserProfileDto(@Nonnull final UserProfile request) {
		UserProfileDto userProfileDto = new UserProfileDto();
		userProfileDto.setAddress(request.getAddress());
		userProfileDto.setDateOfBirth(request.getDateOfBirth());
		userProfileDto.setDescription(request.getDescription());
		userProfileDto.setEducation(request.getEducation());
		userProfileDto.setExperiences(request.getExperiences());
		userProfileDto.setGender(request.getGender());
		userProfileDto.setHobbies(request.getHobbies());
		userProfileDto.setPhoneNumber(request.getPhoneNumber());
		userProfileDto.setUserId(request.getUserId());
		userProfileDto.setId(request.getId());
		return userProfileDto;
	}

}
