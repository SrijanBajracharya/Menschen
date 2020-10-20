package com.achiever.menschenfahren.entities.events;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.models.EventRole;
import com.achiever.menschenfahren.models.EventStatus;

import lombok.Data;
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString
@Entity
@Table(name = "event_participants")
public class EventParticipant {

	@Id
	@Nonnull
	private String id;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", referencedColumnName = "id")
	private Event event;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private EventRole role;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private EventStatus status;

	public EventParticipant() {
		this.id = UUID.randomUUID().toString();
	}

}
