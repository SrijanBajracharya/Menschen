package com.achiever.menschenfahren.entities.roles;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString
@Entity
@Table(name = "role_permissions")
public class RolePermissions {

	@Id
	@Nonnull
	private String id;

	@ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private Set<Role> Role;

	@Column(name = "section")
	private String section;

	@Column(name = "created_timestamp", nullable = false, updatable = false)
	private Date createdTimestamp;

	@Column(name = "modified_timestamp", nullable = false)
	private Date modifiedTimestamp;

	public RolePermissions() {
		this.id = UUID.randomUUID().toString();
		this.createdTimestamp = new Date();
		this.modifiedTimestamp = createdTimestamp;
	}

}
