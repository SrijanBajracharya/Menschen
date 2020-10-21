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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString(callSuper = true)
@Entity(name = "role_permissions")
@Table
@EqualsAndHashCode(callSuper = true)
public class RolePermissions extends AbstractBaseEntity {

	@Id
	@Nonnull
	private String id;

	@ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id")
	private Set<Role> Role;

	@Column(name = "section")
	private String section;

	@Column(name = "modified_timestamp", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedTimestamp;

	public RolePermissions() {
		this.id = UUID.randomUUID().toString();
		this.modifiedTimestamp = createdTimestamp;
	}

}
