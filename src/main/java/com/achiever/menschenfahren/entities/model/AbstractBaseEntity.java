package com.achiever.menschenfahren.entities.model;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractBaseEntity {

	/**
	 * Local for each database. Indicateds when it was last updated inside the
	 * database
	 **/
	@Column(name = "update_db", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Nonnull
	protected Date updateDb;

	@Column(name = "created_timestamp", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Nonnull
	protected Date createdTimestamp;

	public AbstractBaseEntity() {
		this.updateDb = new Date();
		this.createdTimestamp = updateDb;
	}

	@PreUpdate
	@PrePersist
	public void updateTimestamps() {
		this.updateDb = new Date();
	}
}
