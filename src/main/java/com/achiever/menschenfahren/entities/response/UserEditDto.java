package com.achiever.menschenfahren.entities.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Srijan Bajracharya
 *
 */
@Schema
@Data
@NoArgsConstructor
public class UserEditDto implements RestOperationDto {

	private static final long serialVersionUID = 5572192286629553275L;

	/** First name of user **/
	private String firstName;

	/**
	 * Last name of user
	 */
	private String lastName;

	/**
	 * Email id of user.
	 */
	private String email;

}
