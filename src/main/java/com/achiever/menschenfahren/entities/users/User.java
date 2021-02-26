package com.achiever.menschenfahren.entities.users;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.achiever.menschenfahren.base.model.AuthProviderType;
import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString(callSuper = true)
@Entity(name = "users")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractBaseEntity {

    @Id
    @Nonnull
    private String           id;

    @Column(name = "first_name", length = 100)
    @Nonnull
    private String           firstName;

    @Column(name = "last_name", length = 100)
    @Nonnull
    private String           lastName;

    @Column(name = "email", length = 50)
    @Nonnull
    private String           email;

    @Column(name = "username", length = 50)
    private String           username;

    @Column(name = "password", length = 600)
    @Nonnull
    private String           password;

    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    private AuthProviderType authenticationType;

    @Column(name = "modified_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date             modifiedTimestamp;

    @Column(name = "voided")
    private boolean          voided;

    @Column(name = "is_active")
    private boolean          isActive;

    @Column(name = "deactivated_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date             deactivatedTimestamp;

    public User() {
        super();
        this.id = UUID.randomUUID().toString();
        this.modifiedTimestamp = createdTimestamp;
        this.isActive = true;
        this.voided = false;
        // this.authenticationType = AuthProviderType.OTHER;
        // this.deactivatedTimestamp = null;
    }

}
