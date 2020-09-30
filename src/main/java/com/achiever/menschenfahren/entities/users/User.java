package com.achiever.menschenfahren.entities.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.achiever.menschenfahren.models.AuthProviderType;

import lombok.Data;
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "first_name", length = 100)
	private String firstName;

	@Column(name = "last_name", length = 100)
	private String lastName;

	@Column(name = "email", length = 50)
	private String email;

	@Column(name = "password", length = 600)
	private String password;

	@Column(name = "auth_provider")
	@Enumerated(EnumType.STRING)
	private AuthProviderType authenticationType;

	@Column(name = "created_timestamp")
	private long createdTimestamp;

	@Column(name = "voided")
	private boolean voided;

	@Column(name = "is_active")
	private boolean isActive = true;

	@Column(name = "deactivated_timestamp")
	private long deactivatedTimestamp;

}
