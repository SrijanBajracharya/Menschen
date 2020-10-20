package com.achiever.menschenfahren.entities.events;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "event_gallery")
public class EventGallery extends AbstractBaseEntity implements Serializable {

	private static final long serialVersionUID = -522049643725099917L;

	@Id
	@Nonnull
	private String id;

	@ManyToOne(targetEntity = Event.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", referencedColumnName = "id")
	private Event event;

	@Column(name = "photo")
	private String photo;

	@Column(name = "caption")
	private String caption;

	@Column(name = "uploaded_by")
	private String uploadedBy;

	@Column(name = "uploaded_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadedOn;

	@Column(name = "modified_timestamp", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedTimestamp;

	public EventGallery() {
		super();
		this.id = UUID.randomUUID().toString();
		this.modifiedTimestamp = createdTimestamp;
	}

}
