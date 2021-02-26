package com.achiever.menschenfahren.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.achiever.menschenfahren.base.dto.request.FeedbackCreateDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.FeedbackDto;
import com.achiever.menschenfahren.exception.InvalidFeedbackException;

import io.swagger.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Interface for handling feedaback features.
 *
 * @author Srijan Bajracharya
 *
 */
public interface FeedbackRestControllerInterface {

    /**
     * Create feedback from the user.
     *
     * @param request
     *            The request to be saved.
     * @return
     * @throws InvalidFeedbackException
     *             Thrown if the request is invalid.
     */
    @Operation(description = "Creates a new Feedback.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feedback was successfully created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FeedbackDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Returned if the Feedback data contained invalid field") })
    @PostMapping("feedback")
    ResponseEntity<DataResponse<FeedbackDto>> CreateFeedback(@RequestBody(required = true) @Valid final FeedbackCreateDto request)
            throws InvalidFeedbackException;

    /**
     * Get all feedbacks from the users.
     *
     * @return {@link List} of feedbacks.
     */
    @Operation(description = "Return All Feedbacks.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found Feedbacks", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FeedbackDto.class)) }) })
    @GetMapping("feedbacks")
    ResponseEntity<DataResponse<List<FeedbackDto>>> getFeedbacks();

}
