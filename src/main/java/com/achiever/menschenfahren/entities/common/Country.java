package com.achiever.menschenfahren.entities.common;

import java.util.UUID;

import javax.annotation.Nonnull;
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
	@Nonnull
	private String id;

	@Nonnull
	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "name", nullable = false)
	private String name;

	public Country() {
		this.id = UUID.randomUUID().toString();
	}

}
