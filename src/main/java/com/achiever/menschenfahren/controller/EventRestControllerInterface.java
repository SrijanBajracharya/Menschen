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
import com.achiever.menschenfahren.entities.response.EventCreateDto;
import com.achiever.menschenfahren.entities.response.EventDto;
import com.achiever.menschenfahren.exception.InvalidEventException;

import io.swagger.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface EventRestControllerInterface {

	/**
	 * Returns the events a specific event can see.
	 *
	 * @param alsoVoided If voided events are also considered.
	 * @return The list of events, an empty list and code 204 or an error code.
	 */
	@Operation(description = "Return All Events.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found Events", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventDto.class)) }),
			@ApiResponse(responseCode = "204", description = "Found no visible Event", content = @Content()),
			@ApiResponse(responseCode = "400", description = "The event details is incomplete"),
			@ApiResponse(responseCode = "410", description = "The event has been voided") })
	@Parameters(value = {
			@Parameter(name = CommonConstants.Params.ALSO_VOIDED, description = "Optional filter if voided events are also considered and returned.") })
	@GetMapping("events")
	ResponseEntity<DataResponse<List<EventDto>>> getEvents(
			@RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided,
			@RequestParam(name = CommonConstants.Params.ALSO_PRIVATE, defaultValue = "false", required = false) final boolean alsoPrivate)
			throws InvalidEventException;

	/**
	 * Returns a specific event based on eventId.
	 *
	 * @param eventId    The identifier for the event.
	 * @param alsoVoided If voided spaces are returned.
	 * @return
	 */
	@Operation(description = "Returns the event  with the given eventId.")
	@Parameters(value = {
			@Parameter(name = CommonConstants.Params.ALSO_VOIDED, description = "If voided events are also considered and returned.") })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the Event", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventDto.class)) }),
			@ApiResponse(responseCode = "400", description = "The event details is incomplete"),
			@ApiResponse(responseCode = "410", description = "The event has been voided"),
			@ApiResponse(responseCode = "404", description = "No Event found with the eventId") })
	@GetMapping("events/{eventId}")
	ResponseEntity<DataResponse<EventDto>> getEvent(
			@PathVariable(name = "eventId", required = true) @Nonnull final String eventId,
			@RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided,
			@RequestParam(name = CommonConstants.Params.ALSO_PRIVATE, defaultValue = "false", required = false) final boolean alsoPrivate)
			throws InvalidEventException;

	/**
	 * Creates a new Event.
	 *
	 * @param request The request for creating an event.
	 * @return
	 * @throws InvalidEventException
	 */
	@Operation(description = "Creates a new Event.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Event was successfully created", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventDto.class)) }),
			@ApiResponse(responseCode = "400", description = "Returned if the event data contained invalid field") })
	@PostMapping("events")
	ResponseEntity<DataResponse<EventDto>> createEvent(
			@RequestBody(required = true) @Valid final EventCreateDto request) throws InvalidEventException;

	/**
	 * Finds events for an user.
	 *
	 * @param userId     The identifier of an user.
	 * @param alsoVoided If voided events are also considered.
	 * @return {@link List} of events.
	 * @throws InvalidEventException
	 */
	@Operation(description = "Returns all Events for an user")
	@Parameters(value = {
			@Parameter(name = CommonConstants.Params.ALSO_VOIDED, description = "If voided events are also considered and returned.") })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found Events for an user", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventDto.class)) }),
			@ApiResponse(responseCode = "400", description = "Returned if the event data contained invalid field"),
			@ApiResponse(responseCode = "410", description = "The event has been voided"),
			@ApiResponse(responseCode = "204", description = "Found no visible Event", content = @Content()) })
	@GetMapping("events/{userId}")
	ResponseEntity<DataResponse<List<EventDto>>> getEventsByUserId(
			@PathVariable(name = "userId", required = true) @Nonnull final String userId,
			@RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided)
			throws InvalidEventException;

}
