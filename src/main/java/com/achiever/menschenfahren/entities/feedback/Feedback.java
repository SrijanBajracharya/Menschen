package com.achiever.menschenfahren.entities.feedback;

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
@Entity(name = "feedback")
@Table
@EqualsAndHashCode(callSuper = true)
public class Feedback extends AbstractBaseEntity {

    @Id
    @Nonnull
    private String id;

    @Nonnull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "subject", length = 100, nullable = false)
    @Nonnull
    private String subject;

    @Column(name = "description", nullable = false)
    @Nonnull
    private String description;

    public Feedback() {
        super();
        this.id = UUID.randomUUID().toString();

    }
}
