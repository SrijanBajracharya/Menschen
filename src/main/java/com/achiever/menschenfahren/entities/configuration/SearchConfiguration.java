package com.achiever.menschenfahren.entities.configuration;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@Entity(name = "search_config")
@Table
@EqualsAndHashCode(callSuper = true)
public class SearchConfiguration extends AbstractBaseEntity {

    @Id
    @Nonnull
    private String id;

    @Column(name = "user_id")
    @Nonnull
    private String userId;

    @Column(name = "event_type_id")
    private String eventTypeId;

    @Column(name = "country_id")
    private String countryId;

    @Column(name = "destination")
    private String destination;

    @Column(name = "age_group")
    private String ageGroup;

    @Column(name = "modified_Timestamp", nullable = false)
    private Date   modifiedTimestamp;

    public SearchConfiguration() {
        super();
        this.id = UUID.randomUUID().toString();
        this.modifiedTimestamp = this.createdTimestamp;
    }
}
