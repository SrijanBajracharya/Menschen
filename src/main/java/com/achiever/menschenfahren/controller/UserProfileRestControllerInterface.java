package com.achiever.menschenfahren.controller;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.achiever.menschenfahren.constants.CommonConstants;
import com.achiever.menschenfahren.entities.response.DataResponse;
import com.achiever.menschenfahren.entities.response.EventDto;
import com.achiever.menschenfahren.entities.response.UserProfileCreateDto;
import com.achiever.menschenfahren.entities.response.UserProfileDto;
import com.achiever.menschenfahren.entities.response.UserProfileEditDto;
import com.achiever.menschenfahren.exception.InvalidUserException;
import com.achiever.menschenfahren.exception.ResourceNotFoundException;

import io.swagger.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface UserProfileRestControllerInterface {
    /**
     * Creates user profile.
     *
     * @param userId
     *            The id of the user.
     * @param request
     *            The set of data to be saved.
     * @param alsoVoided
     *            The flag to check if the profile is voided or not.
     * @return The saved user profile
     * @throws InvalidUserException
     *             Thrown if the user is invalid.
     */
    @Operation(description = "Creates user profile for the given userId.")
    @Parameters(value = { @Parameter(name = CommonConstants.Params.ALSO_VOIDED, description = "If voided users are also considered and returned.") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "UserProfile was successfully created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserProfileCreateDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Returned if the User Profile data contained invalid field"),
            @ApiResponse(responseCode = "404", description = "The user with the given id doesn't exist", content = @Content()) })
    @PostMapping("userProfile/{userId}")
    ResponseEntity<DataResponse<UserProfileDto>> createProfile(@PathVariable(name = "userId", required = true) @Nonnull final String userId,
            @RequestBody(required = true) @Valid final UserProfileCreateDto request,
            @RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided)
            throws InvalidUserException;

    /**
     * Fetches user Profile by id.
     *
     * @param id
     *            The identifier of user profile
     * @return The user profile.
     * @throws ResourceNotFoundException
     *             Thrown if the userProfile cannot be found for given id.
     */
    @Operation(description = "Fetch user profile from the provided user profile id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UserProfile was successfully Fetched", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserProfileDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Returned if the User Profile data contained invalid field") })
    @GetMapping("userProfile/{id}")
    ResponseEntity<DataResponse<UserProfileDto>> getUserProfileById(@PathVariable(name = "id", required = true) @Nonnull final String id)
            throws ResourceNotFoundException;

    /**
     * Updates the given Event.
     *
     * @param eventId
     *            The id of the event as part of the path.
     * @param request
     *            The set of updated values.
     * @return The updated event or an error code.
     * @throws ResourceNotFoundException
     *             If the referenced Id wasn't found. Will be returned as code 404.
     */
    @Operation(description = "Updates an exisiting user profile.")
    @Parameters(value = { @Parameter(name = "request", description = "The fields as request body that can be changed during a basic edit operation"),
            @Parameter(name = CommonConstants.Params.USER_ID, description = "The id of the user as part of the path.") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile was successfully edited", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventDto.class)) }),
            @ApiResponse(responseCode = "400", description = "The given user wasn't valid for an update operation."),
            @ApiResponse(responseCode = "404", description = "The user with the given id doesn't exist", content = @Content()) })
    @PatchMapping("userProfile/{" + CommonConstants.Params.USER_PROFILE_ID + "}/edit")
    ResponseEntity<DataResponse<UserProfileDto>> editProfile(
            @PathVariable(name = CommonConstants.Params.USER_PROFILE_ID, required = true) @Nonnull final String userProfileId,
            @RequestBody(required = true) @Valid final UserProfileEditDto request) throws ResourceNotFoundException;
}
