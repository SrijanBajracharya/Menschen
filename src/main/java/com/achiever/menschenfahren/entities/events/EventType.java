package com.achiever.menschenfahren.entities.events;

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
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString
@Entity
@Table(name = "event_types")
public class EventType extends AbstractBaseEntity {

	@Id
	@Nonnull
	private String id;

	@Column(name = "name", nullable = false)
	@Nonnull
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "modified_timestamp", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedTimestamp;

	public EventType() {
		super();
		this.id = UUID.randomUUID().toString();
		this.modifiedTimestamp = createdTimestamp;
	}
}
