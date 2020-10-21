package com.achiever.menschenfahren.entities.roles;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString(callSuper = true)
@Entity(name = "roles")
@Table
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractBaseEntity {

	@Id
	@Nonnull
	private String id;

	@Column(name = "name", length = 30)
	@Nonnull
	private String name;

	@Column(name = "description")
	@Nullable
	private String description;

	@Column(name = "modified_timestamp", nullable = false)
	private Date modifiedTimestamp;

	public Role() {
		this.id = UUID.randomUUID().toString();
		this.modifiedTimestamp = createdTimestamp;
	}
}
