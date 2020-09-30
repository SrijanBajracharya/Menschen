package com.achiever.menschenfahren.entities.users;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.achiever.menschenfahren.entities.roles.Role;
import com.achiever.menschenfahren.models.Gender;

import lombok.Data;
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString
@Entity
@Table(name = "user_profiles")
public class UserProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User userId;

	@OneToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private List<Role> roleId;

	@Column(name = "dob")
	private String dateOfBirth;

	@Column(name = "terms_and_agreement")
	private boolean termsAndAgreement = true;

	@Column(name = "timezone")
	private String timezone;

	@Column(name = "photo")
	private String photo;

	@Column(name = "address")
	private String address;

	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(name = "phone_number", length = 30)
	private String phoneNumber;

	@Column(name = "is_phone_number_valid")
	private boolean isPhoneNoValidated;

	@Column(name = "description", length = 1500)
	private String description;

	@Column(name = "education")
	private String education;

	@Column(name = "hobbies")
	private String hobbies;

	@Column(name = "experiences")
	private String experiences;

	@Column(name = "verification_document")
	private String verificationDocument;

	@Column(name = "voided")
	private boolean voided;

}
