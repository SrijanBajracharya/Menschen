package com.achiever.menschenfahren.entities.events;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;
import com.achiever.menschenfahren.entities.users.User;

import lombok.Data;
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString
@Entity
@Table(name = "event_log", indexes = { @Index(columnList = "created_timestamp"),
		@Index(columnList = " event_id, user_id") })
public class EventLog extends AbstractBaseEntity {

	@Id
	@Nonnull
	private String id;

	@Column(name = "description")
	private String description;

	@ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", referencedColumnName = "id")
	private Event event;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column(name = "modified_timestamp", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedTimestamp;

	public EventLog() {
		super();
		this.id = UUID.randomUUID().toString();
		this.modifiedTimestamp = createdTimestamp;

	}

}
