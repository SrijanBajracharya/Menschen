package com.achiever.menschenfahren.entities.users;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;
import com.achiever.menschenfahren.entities.roles.Role;
import com.achiever.menschenfahren.models.Gender;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * CreatedBy : edangol CreatedOn : 10/04/2020 Description :
 **/
@Data
@ToString(callSuper = true)
@Entity
@Table(name = "user_profiles")
@EqualsAndHashCode(callSuper = true)
public class UserProfile extends AbstractBaseEntity {

    @Id
    @Nonnull
    private String     id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User       user;

    @OneToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private List<Role> roleId;

    @Column(name = "dob")
    private String     dateOfBirth;

    @Column(name = "terms_and_agreement")
    private boolean    termsAndAgreement = true;

    @Column(name = "timezone")
    private String     timezone;

    @Column(name = "photo")
    private String     photo;

    @Column(name = "address")
    @Nullable
    private String     address;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender     gender;

    @Column(name = "phone_number", length = 30)
    private String     phoneNumber;

    @Column(name = "is_phone_number_valid")
    private boolean    isPhoneNoValidated;

    @Column(name = "description", length = 1500)
    @Nullable
    private String     description;

    @Column(name = "education")
    @Nullable
    private String     education;

    @Column(name = "hobbies")
    @Nullable
    private String     hobbies;

    @Column(name = "experiences")
    @Nullable
    private String     experiences;

    @Column(name = "verification_document")
    private String     verificationDocument;

    @Column(name = "voided")
    private boolean    voided;

    @Column(name = "modified_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date       modifiedTimestamp;

    public UserProfile() {
        super();
        this.id = UUID.randomUUID().toString();
        this.modifiedTimestamp = createdTimestamp;
    }

}
