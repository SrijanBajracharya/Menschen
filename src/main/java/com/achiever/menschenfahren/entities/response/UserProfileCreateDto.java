package com.achiever.menschenfahren.entities.response;

import com.achiever.menschenfahren.entities.users.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserProfileCreateDto extends UserProfileEditDto {

	private static final long serialVersionUID = 1786328631219102604L;

	@Schema(description = "The id of an user")
	private User userId;

}
