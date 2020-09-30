package com.achiever.menschenfahren.entities.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

/**
 *
 * @author Srijan Bajracharya
 *
 */
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
