package com.achiever.menschenfahren.entities.events;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@Entity(name = "favorite")
@Table
@EqualsAndHashCode(callSuper = true)
public class Favorites extends AbstractBaseEntity {

    @Id
    @Nonnull
    private String id;

    @Nonnull
    @Column(name = "user_id")
    private String userId;

    @OneToOne(targetEntity = Event.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event  event;

    public Favorites() {
        super();
        this.id = UUID.randomUUID().toString();
    }

}
