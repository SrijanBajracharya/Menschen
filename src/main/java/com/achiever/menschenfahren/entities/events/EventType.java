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
import javax.persistence.UniqueConstraint;

import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString(callSuper = true)
@Entity
@Table(name = "event_types", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
@EqualsAndHashCode(callSuper = true)
public class EventType extends AbstractBaseEntity {

    @Id
    @Nonnull
    private String  id;

    @Column(name = "name", nullable = false)
    @Nonnull
    private String  name;

    @Column(name = "description", nullable = false)
    @Nonnull
    private String  description;

    @Column(name = "modified_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date    modifiedTimestamp;

    @Column(name = "voided")
    private boolean voided;

    public EventType() {
        super();
        this.id = UUID.randomUUID().toString();
        this.modifiedTimestamp = createdTimestamp;
    }

}
