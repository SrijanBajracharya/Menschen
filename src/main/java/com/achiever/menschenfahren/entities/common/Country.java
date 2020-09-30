package com.achiever.menschenfahren.entities.common;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * CreatedBy : edangol
 * CreatedOn : 10/04/2020
 * Description :
 **/
@Data
@ToString
@Entity
@Table(name = "countries")
public class Country {

    @Id
    private String code;

    @Column(name = "name")
    private String name;

}
