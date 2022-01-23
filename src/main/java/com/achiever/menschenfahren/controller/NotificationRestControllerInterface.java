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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.achiever.menschenfahren.base.constants.CommonConstants;
import com.achiever.menschenfahren.base.dto.request.NotificationCreateDto;
import com.achiever.menschenfahren.base.dto.request.NotificationEditDto;
import com.achiever.menschenfahren.base.dto.request.NotificationInviteDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.NotificationDto;
import com.achiever.menschenfahren.exception.InvalidNotificationException;
import com.achiever.menschenfahren.exception.ResourceNotFoundException;

import io.swagger.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * The interface for handling all functionality related to notification.
 *
 * @author Srijan Bajracharya
 *
 */
public interface NotificationRestControllerInterface {

    /**
     * Creates a new request to join Notification.
     *
     * @param request
     *            The object to create a new Notification.
     * @return The created notification.
     * @throws InvalidNotificationException
     *             Thrown if the notification request is invalid.
     * @throws ResourceNotFoundException
     */
    @Operation(description = "Creates a new Notification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification was successfully created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationDto.class)) }),
            @ApiResponse(responseCode = "208", description = "Returned if the Notification has already been sent."),
            @ApiResponse(responseCode = "400", description = "Returned if the Notification data contained invalid field") })
    @PostMapping("notification/requestToJoin")
    ResponseEntity<DataResponse<NotificationDto>> createJoinRequest(@RequestBody(required = true) @Valid final NotificationCreateDto request,
            @RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided)
            throws ResourceNotFoundException, InvalidNotificationException;

    /**
     * Creates a new invite notification.
     *
     * @param request
     *            The object to create a new notification.
     * @return Created notification.
     * @throws InvalidNotificationException
     *             Thrown if the notification request is invalid.
     * @throws ResourceNotFoundException
     */
    @Operation(description = "Creates a new Notification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification was successfully created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Returned if the Notification data contained invalid field"),
            @ApiResponse(responseCode = "204", description = "Returned if the Notification couldn't be saved."),
            @ApiResponse(responseCode = "208", description = "Returned if the Notification has already been sent."),
            @ApiResponse(responseCode = "404", description = "Returned if the receiver email id is not found in the system.") })
    @PostMapping("notification/invite")
    ResponseEntity<DataResponse<NotificationDto>> createInviteRequest(@RequestBody(required = true) @Valid final NotificationInviteDto request)
            throws InvalidNotificationException, ResourceNotFoundException;

    /***
     * Updates the state of the notification.
     *
     * @param notificationId
     *            The id of notification to be updated.
     * @param originalSenderId
     *            The user who created the original notification.
     * @param originalRecieverId
     *            The user who receives the notification-
     * @param request
     *            The request which needs to be updated.
     * @return The updated notification object.
     * @throws ResourceNotFoundException
     *             If the id of notification not found.
     * @throws InvalidNotificationException
     *             The request data is not valid.
     */
    @Operation(description = "Updates an exisiting notification.")
    @Parameters(value = {
            @Parameter(name = CommonConstants.Params.NOTIFICATION_ID, description = "The id of the notification as part of the path."),
            @Parameter(name = CommonConstants.Params.SENDER_ID, description = "The original sender id of the notification as part of the path."),
            @Parameter(name = CommonConstants.Params.RECEIVER_ID, description = "The original receiver id of the notification as part of the path.") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification was successfully edited", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationDto.class)) }),
            @ApiResponse(responseCode = "400", description = "The given notification wasn't valid for an update operation."),
            @ApiResponse(responseCode = "404", description = "The notification with the given id doesn't exist", content = @Content()) })
    @PatchMapping("notification/{" + CommonConstants.Params.NOTIFICATION_ID + "}/sender/{" + CommonConstants.Params.SENDER_ID + "}")
    ResponseEntity<DataResponse<NotificationDto>> updateNotification(
            @PathVariable(name = CommonConstants.Params.NOTIFICATION_ID, required = true) @Nonnull final String notificationId,
            @PathVariable(name = CommonConstants.Params.SENDER_ID, required = true) @Nonnull final String originalSenderId,
            @RequestBody(required = true) @Valid final NotificationEditDto request) throws ResourceNotFoundException, InvalidNotificationException;

    /**
     * Returns all notification related to the user
     *
     * @param alsoVoided
     * @return Returns the notification dto object.
     * @throws InvalidNotificationException
     *             Thrown fi the notification is invalid.
     */
    @Operation(description = "Return All Notifications.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Notifications", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationDto.class)) }),
            @ApiResponse(responseCode = "204", description = "Found no visible Notification", content = @Content()),
            @ApiResponse(responseCode = "400", description = "The notification data is incomplete"),
            @ApiResponse(responseCode = "410", description = "The notification has been voided") })
    @GetMapping("notification")
    ResponseEntity<DataResponse<List<NotificationDto>>> ggetNotificationByToken(
            @RequestParam(name = CommonConstants.Params.ALSO_VOIDED, defaultValue = "false", required = false) final boolean alsoVoided)
            throws InvalidNotificationException;

}
