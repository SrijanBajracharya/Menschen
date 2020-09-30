package com.achiever.menschenfahren.entities.response;

import java.util.List;

import com.achiever.menschenfahren.entities.roles.Role;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.models.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema
@Data
@NoArgsConstructor
public class UserProfileEditDto {

	private User userId;

	private List<Role> roleId;

	private String dateOfBirth;

	private boolean termsAndAgreement = true;

	private String timezone;

	private String photo;

	private String address;

	private Gender gender;

	private String phoneNumber;

	private String description;

	private String education;

	private String hobbies;

	private String experiences;

}
