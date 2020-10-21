package com.achiever.menschenfahren.entities.common;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Srijan Bajracharya
 *
 */
@Data
@ToString(callSuper = true)
@Entity(name = "countries")
@Table
@EqualsAndHashCode(callSuper = true)
public class Country extends AbstractBaseEntity {

	@Id
	@Nonnull
	private String id;

	@Nonnull
	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "modified_timestamp", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedTimestamp;

	public Country() {
		this.id = UUID.randomUUID().toString();
		this.modifiedTimestamp = createdTimestamp;
	}

}
