package com.achiever.menschenfahren.controller;

import java.util.List;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.achiever.menschenfahren.constants.CommonConstants;
import com.achiever.menschenfahren.entities.response.DataResponse;
import com.achiever.menschenfahren.entities.response.UserCreateDto;
import com.achiever.menschenfahren.entities.response.UserDto;
import com.achiever.menschenfahren.entities.response.UserProfileEditDto;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;
import com.achiever.menschenfahren.exception.InvalidEventException;
import com.achiever.menschenfahren.exception.InvalidUserException;

import io.swagger.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 *
 * @author Srijan Bajracharya
 *
 */
public interface UserRestControllerInterface {

	/**
	 * Creates a new User.
	 *
	 * @param request The object to create a new user.
	 * @return
	 * @throws InvalidEventException
	 */
	@Operation(description = "Creates a new User.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User was successfully created", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class)) }),
			@ApiResponse(responseCode = "400", description = "Returned if the User data contained invalid field") })
	@PostMapping("user")
	ResponseEntity<DataResponse<UserDto>> createUser(@RequestBody(required = true) @Valid final UserCreateDto request,
			@RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided)
			throws InvalidUserException;

	/**
	 * Returns all the users based on the voided filter.
	 *
	 * @param alsoVoided Checks if the user is active or deactivated.
	 * @return
	 * @throws InvalidEventException
	 */
	@Operation(description = "Return All Users.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found Users", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "204", description = "Found no visible User", content = @Content()),
			@ApiResponse(responseCode = "400", description = "The user details is incomplete"),
			@ApiResponse(responseCode = "410", description = "The user has been voided") })
	@Parameters(value = {
			@Parameter(name = CommonConstants.Params.ALSO_VOIDED, description = "Optional filter if voided events are also considered and returned.") })
	@GetMapping("users")
	ResponseEntity<DataResponse<List<User>>> getUsers(
			@RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided)
			throws InvalidUserException;

	/**
	 * Returns the user based on userId
	 *
	 * @param userId     The identifier of the user
	 * @param alsoVoided Active or deactive user.
	 * @return
	 * @throws InvalidEventException
	 */
	@Operation(description = "Returns the user  with the given userId.")
	@Parameters(value = {
			@Parameter(name = CommonConstants.Params.ALSO_VOIDED, description = "If voided users are also considered and returned.") })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the User", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "400", description = "The user details is incomplete"),
			@ApiResponse(responseCode = "410", description = "The user has been voided"),
			@ApiResponse(responseCode = "404", description = "No user found with the userId") })
	@GetMapping("users/{userId}")
	ResponseEntity<DataResponse<User>> getUser(
			@PathVariable(name = "userId", required = true) @Nonnull final String userId,
			@RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided)
			throws InvalidUserException;

	/**
	 *
	 * @param userId
	 * @param request
	 * @param alsoVoided
	 * @return
	 * @throws InvalidUserException
	 */
	@Operation(description = "Creates user profile for the given userId.")
	@Parameters(value = {
			@Parameter(name = CommonConstants.Params.ALSO_VOIDED, description = "If voided users are also considered and returned.") })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "UserProfile was successfully created", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserProfile.class)) }),
			@ApiResponse(responseCode = "400", description = "Returned if the User Profile data contained invalid field") })
	@PostMapping("userProfile/{userId}")
	ResponseEntity<DataResponse<UserProfile>> createProfile(
			@PathVariable(name = "userId", required = true) @Nonnull final String userId,
			@RequestBody(required = true) @Valid final UserProfileEditDto request,
			@RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided)
			throws InvalidUserException;

}
