package com.achiever.menschenfahren.models;

public enum AppRole {
	ADMIN("admin"), USER("user");

	private String value;

	AppRole(String role) {
		this.value = role;
	}

	public String getValue() {
		return value;
	}
}
