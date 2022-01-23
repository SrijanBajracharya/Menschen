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

import com.achiever.menschenfahren.base.constants.CommonConstants;
import com.achiever.menschenfahren.base.dto.request.FavoriteCreateDto;
import com.achiever.menschenfahren.base.dto.response.AllFavoritesResponse;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.FavoritesDto;
import com.achiever.menschenfahren.exception.InvalidFavoriteException;
import com.achiever.menschenfahren.exception.ResourceNotFoundException;

import io.swagger.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Interface handling all functionality related to favorites.
 *
 * @author Srijan Bajracharya
 *
 */
public interface FavoritesRestControllerInterface {

    /**
     * Creates/Removes favorite event. If present removes else creates new favorite.
     *
     * @param request
     *            The request to be created or removed.
     * @return The created event or empty gone response.
     * @throws InvalidFavoriteException
     *             Thrown if the request is invalid.
     * @throws ResourceNotFoundException
     *             Thrown if the event is not found in the system.
     */
    @Operation(description = "Creates and deletes Favorite. If the favorite event is already present for a given user then it removes else creates favorite.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Favorite was successfully created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FavoritesDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Returned if the Favorite data contained invalid field"),
            @ApiResponse(responseCode = "404", description = "Returned if the event id is not found in the system."),
            @ApiResponse(responseCode = "200", description = "Returned if the event is removed for the given user.") })
    @PostMapping("favorites")
    ResponseEntity<DataResponse<FavoritesDto>> createAndRemoveFavorite(@RequestBody(required = true) @Valid final FavoriteCreateDto request)
            throws InvalidFavoriteException, ResourceNotFoundException;

    /**
     * Returns all favorites for a provided user token.
     *
     * @param userId
     *            The id of an user.
     * @return The list of favorites
     * @throws InvalidFavoriteException
     *             Thrown if the request is invalid.
     */
    @Operation(description = "Return All Favorites for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Favorites", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AllFavoritesResponse.class)) }),
            @ApiResponse(responseCode = "204", description = "Found no visible Favorites", content = @Content()),
            @ApiResponse(responseCode = "400", description = "The request is incomplete") })
    @GetMapping("favorites")
    ResponseEntity<DataResponse<List<AllFavoritesResponse>>> getFavorites() throws InvalidFavoriteException;

    
    /**
     * Removes favorite and returns updated list.
     *
     * @param request
     *            The request to remove favorite.
     * @return The updated favorite list.
     * @throws InvalidFavoriteException
     *             Thrown if the request is invalid.
     * @throws ResourceNotFoundException
     *             Thrown if resource(event) is not found in the system.
     */
    @Operation(description = "Remove the favorite and Returns All Favorites for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite removed successfully", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AllFavoritesResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Returned if the event id is not found in the system."),
            @ApiResponse(responseCode = "400", description = "The request is incomplete") })
    @PostMapping("favorites/remove")
    ResponseEntity<DataResponse<List<AllFavoritesResponse>>> removeFavorite(@RequestBody(required = true) @Valid final FavoriteCreateDto request)
            throws InvalidFavoriteException, ResourceNotFoundException;
}
