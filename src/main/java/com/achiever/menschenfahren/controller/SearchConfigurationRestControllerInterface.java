package com.achiever.menschenfahren.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.achiever.menschenfahren.base.dto.request.SearchConfigCreateDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.SearchConfigResponseDto;
import com.achiever.menschenfahren.exception.InvalidSearchConfigException;
import com.achiever.menschenfahren.exception.ResourceNotFoundException;

import io.swagger.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface SearchConfigurationRestControllerInterface {

    @Operation(description = "Creates a new Search config.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Search config was successfully created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SearchConfigResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Returned if the search config data contained invalid field"),
            @ApiResponse(responseCode = "404", description = "Returned if the user id not found.") })
    @PostMapping("searchConfig")
    ResponseEntity<DataResponse<SearchConfigResponseDto>> createSearchConfig(@RequestBody(required = true) @Valid final SearchConfigCreateDto request)
            throws InvalidSearchConfigException, ResourceNotFoundException;

}
