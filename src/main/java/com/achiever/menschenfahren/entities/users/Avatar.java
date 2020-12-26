package com.achiever.menschenfahren.entities.users;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@Entity(name = "avatar")
@Table
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class Avatar extends AbstractBaseEntity {

    @Nonnull
    @Id
    private String id;

    @Column(name = "user_id")
    @Nonnull
    private String userId;

    @Column(name = "avatar_type")
    @Nullable
    private String avatarType;

    @Column(name = "avatar")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Nullable
    private byte[] avatar;

    @Column(name = "modified_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date   modifiedTimestamp;

    public Avatar() {
        super();
        this.id = UUID.randomUUID().toString();
        this.modifiedTimestamp = createdTimestamp;
    }
}
