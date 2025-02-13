package com.achiever.menschenfahren.entities.users;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.achiever.menschenfahren.base.model.AppRole;
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
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "email", "username" }) })
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

    @Column(name = "email", length = 50, unique = true, nullable = false)
    @Nonnull
    private String           email;

    @Column(name = "username", length = 50, unique = true, nullable = false)
    @Nonnull
    private String           username;

    @Column(name = "password", length = 600)
    @Nonnull
    private String           password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppRole          role;

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
    @Nullable
    private Date             deactivatedTimestamp;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "friends", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "friend_id")
    @Nonnull
    private Set<String>      friends = new HashSet<>();

    public User() {
        super();
        this.id = UUID.randomUUID().toString();
        this.modifiedTimestamp = createdTimestamp;
        this.isActive = true;
        this.voided = false;
        this.role = AppRole.USER;
    }

}
