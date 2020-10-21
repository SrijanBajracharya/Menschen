package com.achiever.menschenfahren.entities.events;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;
import com.achiever.menschenfahren.entities.users.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString(callSuper = true)
@Entity(name = "event")
@Table
@EqualsAndHashCode(callSuper = true)
public class Event extends AbstractBaseEntity {

	@Id
	@Nonnull
	private String id;

	@ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "location")
	private String location;

	@Column(name = "gallery")
	@OneToMany(targetEntity = EventGallery.class, mappedBy = "event", fetch = FetchType.LAZY)
	private List<EventGallery> gallery;

	@Column(name = "routes")
	@OneToMany(targetEntity = EventRoute.class, mappedBy = "event", fetch = FetchType.LAZY)
	private List<EventRoute> routes;

	@Column(name = "country_code")
	private String countryCode;

	@ManyToOne(targetEntity = EventType.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "event_type_id", referencedColumnName = "id")
	private EventType eventType;

	@Column(name = "description", length = 1000)
	private String description;

	@Column(name = "age_group")
	private String ageGroup;

	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = "number_of_participants")
	private int numberOfParticipants;

	@Column(name = "private", columnDefinition = "boolean default false")
	private boolean isPrivate;

	@Column(name = "voided", columnDefinition = "boolean default false")
	private boolean voided;

	@Column(name = "modified_Timestamp", nullable = false)
	private Date modifiedTimestamp;

	public Event() {
		super();
		this.id = UUID.randomUUID().toString();
		this.modifiedTimestamp = this.createdTimestamp;
	}

}
