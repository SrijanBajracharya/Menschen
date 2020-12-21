package com.achiever.menschenfahren.controller;

import java.util.List;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.achiever.menschenfahren.base.constants.CommonConstants;
import com.achiever.menschenfahren.base.dto.DataResponse;
import com.achiever.menschenfahren.base.dto.EventCreateDto;
import com.achiever.menschenfahren.base.dto.EventDto;
import com.achiever.menschenfahren.base.dto.EventEditDto;
import com.achiever.menschenfahren.exception.InvalidEventException;
import com.achiever.menschenfahren.exception.InvalidUserException;
import com.achiever.menschenfahren.exception.ResourceNotFoundException;

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
     * @param alsoVoided
     *            If voided events are also considered.
     * @return The list of events, an empty list and code 204 or an error code.
     */
    @Operation(description = "Return All Events.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Events", content = {
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
     * @param eventId
     *            The identifier for the event.
     * @param alsoVoided
     *            If voided spaces are returned.
     * @return
     */
    @Operation(description = "Returns the event  with the given eventId.")
    @Parameters(value = { @Parameter(name = CommonConstants.Params.ALSO_VOIDED, description = "If voided events are also considered and returned.") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Event", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventDto.class)) }),
            @ApiResponse(responseCode = "400", description = "The event details is incomplete"),
            @ApiResponse(responseCode = "410", description = "The event has been voided"),
            @ApiResponse(responseCode = "404", description = "No Event found with the eventId") })
    @GetMapping("event/{eventId}")
    ResponseEntity<DataResponse<EventDto>> getEvent(@PathVariable(name = "eventId", required = true) @Nonnull final String eventId,
            @RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided,
            @RequestParam(name = CommonConstants.Params.ALSO_PRIVATE, defaultValue = "false", required = false) final boolean alsoPrivate)
            throws InvalidEventException;

    /**
     * Creates a new Event.
     *
     * @param request
     *            The request for creating an event.
     * @return
     * @throws InvalidEventException
     */
    @Operation(description = "Creates a new Event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event was successfully created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Returned if the event data contained invalid field") })
    @PostMapping("events")
    ResponseEntity<DataResponse<EventDto>> createEvent(@RequestBody(required = true) @Valid final EventCreateDto request) throws InvalidEventException;

    /**
     * Finds events for an user.
     *
     * @param userId
     *            The identifier of an user.
     * @param alsoVoided
     *            If voided events are also considered.
     * @return {@link List} of events.
     * @throws InvalidEventException
     */
    @Operation(description = "Returns all Events for an user")
    @Parameters(value = { @Parameter(name = CommonConstants.Params.ALSO_VOIDED, description = "If voided events are also considered and returned.") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Events for an user", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Returned if the event data contained invalid field"),
            @ApiResponse(responseCode = "410", description = "The event has been voided"),
            @ApiResponse(responseCode = "204", description = "Found no visible Event", content = @Content()) })
    @GetMapping("events/{userId}")
    ResponseEntity<DataResponse<List<EventDto>>> getEventsByUserId(@PathVariable(name = "userId", required = true) @Nonnull final String userId,
            @RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided)
            throws InvalidEventException;

    /**
     * Makes the event private based on the eventId.
     *
     * @param eventId
     *            The identifier of an event.
     * @return The updated Event.
     */
    @Operation(description = "Makes event private and Returns the event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event successfully made private.", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Returned if the event data contained invalid field"),
            @ApiResponse(responseCode = "410", description = "The event is already private."),
            @ApiResponse(responseCode = "404", description = "Found no Event with given eventId", content = @Content()) })
    @PutMapping("events/{eventId}/private")
    ResponseEntity<DataResponse<EventDto>> makeEventPrivate(@PathVariable(name = "eventId", required = true) @Nonnull final String eventId)
            throws ResourceNotFoundException;

    /**
     * Makes teh event public based on the eventId.
     *
     * @param eventId
     *            The identifier of an event.
     * @return The updated Event.
     */
    @Operation(description = "Makes event public and Returns the event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event successfully made public.", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Returned if the event data contained invalid field"),
            @ApiResponse(responseCode = "410", description = "The event is already private."),
            @ApiResponse(responseCode = "404", description = "Found no Event with given eventId", content = @Content()) })
    @PutMapping("events/{eventId}/public")
    ResponseEntity<DataResponse<EventDto>> makeEventPublic(@PathVariable(name = "eventId", required = true) @Nonnull final String eventId)
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
     * @throws InvalidUserException
     */
    @Operation(description = "Updates an exisiting Event.")
    @Parameters(value = { @Parameter(name = "request", description = "The fields as request body that can be changed during a basic edit operation"),
            @Parameter(name = CommonConstants.Params.EVENT_ID, description = "The id of the event as part of the path.") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event was successfully edited", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventDto.class)) }),
            @ApiResponse(responseCode = "400", description = "The given event wasn't valid for an update operation."),
            @ApiResponse(responseCode = "404", description = "The event with the given id doesn't exist", content = @Content()) })
    @PatchMapping("events/{" + CommonConstants.Params.EVENT_ID + "}/edit")
    ResponseEntity<DataResponse<EventDto>> editEvent(@PathVariable(name = CommonConstants.Params.EVENT_ID, required = true) @Nonnull final String eventId,
            @RequestBody(required = true) @Valid final EventEditDto request) throws ResourceNotFoundException, InvalidUserException;

}
