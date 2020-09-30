package com.achiever.menschenfahren.entities.roles;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

/**
 * CreatedBy : edangol
 * CreatedOn : 10/04/2020
 * Description :
 **/
@Data
@ToString
@Entity
@Table(name = "role_permissions")
public class RolePermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Set<Role> Role;

    @Column(name = "section")
    private String section;

}
