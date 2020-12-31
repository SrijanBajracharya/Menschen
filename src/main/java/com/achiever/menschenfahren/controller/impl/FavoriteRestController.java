package com.achiever.menschenfahren.controller.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.base.dto.request.FavoriteCreateDto;
import com.achiever.menschenfahren.base.dto.response.AllFavoritesResponse;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.FavoritesDto;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.controller.FavoritesRestControllerInterface;
import com.achiever.menschenfahren.dao.EventDaoInterface;
import com.achiever.menschenfahren.dao.FavoritesDaoInterface;
import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.events.Favorites;
import com.achiever.menschenfahren.exception.InvalidFavoriteException;
import com.achiever.menschenfahren.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.mapper.FavoritesMapper;

/**
 * Rest controller for handling all features related to favorite.
 *
 * @author Srijan Bajracharya
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
public class FavoriteRestController extends BaseController implements FavoritesRestControllerInterface {

    private final EventDaoInterface     eventDao;

    private final FavoritesDaoInterface favoriteDao;

    private final FavoritesMapper       favoritesMapper;

    public FavoriteRestController(@Nonnull final EventDaoInterface eventDao, @Nonnull final FavoritesDaoInterface favoriteDao) {
        this.eventDao = eventDao;
        this.favoriteDao = favoriteDao;
        this.favoritesMapper = new FavoritesMapper();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<FavoritesDto>> createAndRemoveFavorite(@Nonnull final @Valid FavoriteCreateDto request)
            throws InvalidFavoriteException, ResourceNotFoundException {

        if (StringUtils.isAnyBlank(request.getUserId(), request.getEventId())) {
            throw new InvalidFavoriteException("The request is invalid.");
        }

        Optional<Event> eventOptional = this.eventDao.findById(request.getEventId());
        if (eventOptional.isEmpty()) {
            throw new ResourceNotFoundException("The event not found  with id:" + request.getEventId());
        } else {
            Event event = eventOptional.get();
            Optional<Favorites> favoritesOptional = this.favoriteDao.findByUserIdAndEvent(request.getUserId(), event);

            if (favoritesOptional.isEmpty()) {
                Favorites favorites = createFavorite(request, event);

                Favorites savedFavorites = this.favoriteDao.save(favorites);

                FavoritesDto favoritesDto = this.favoritesMapper.map(savedFavorites, FavoritesDto.class);
                favoritesDto.setEventId(event.getId());

                return buildResponse(favoritesDto, HttpStatus.CREATED);
            } else {
                Favorites favorites = favoritesOptional.get();
                this.favoriteDao.delete(favorites);
                return new ResponseEntity<>(HttpStatus.OK);
            }

        }

    }

    /**
     * Creates Favorite object for saving.
     *
     */
    private Favorites createFavorite(@Nonnull final FavoriteCreateDto request, @Nonnull final Event event) {
        Favorites favorites = new Favorites();
        favorites.setEvent(event);
        favorites.setUserId(request.getUserId());
        return favorites;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<List<AllFavoritesResponse>>> getFavoritesByUserId(@Nonnull final String userId) throws InvalidFavoriteException {

        if (StringUtils.isBlank(userId)) {
            throw new InvalidFavoriteException("The user id should not be blank.");
        }

        return getAllFavoriteDto(userId);

    }

    /**
     * Returns all favorite event dto.
     *
     * @param userId
     *            The id of user.
     * @return The list of favorite events.
     */
    private ResponseEntity<DataResponse<List<AllFavoritesResponse>>> getAllFavoriteDto(@Nonnull final String userId) {
        List<Favorites> favorites = this.favoriteDao.findByUserId(userId);

        List<AllFavoritesResponse> allFavorites = new ArrayList<>();

        for (Favorites favorite : favorites) {
            AllFavoritesResponse favoriteResponseDto = this.favoritesMapper.map(favorite, AllFavoritesResponse.class);
            favoriteResponseDto.getEvent().setUserId(userId);
            allFavorites.add(favoriteResponseDto);

        }

        if (allFavorites.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return buildResponse(allFavorites, HttpStatus.OK);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<List<AllFavoritesResponse>>> removeFavorite(@Valid FavoriteCreateDto request)
            throws InvalidFavoriteException, ResourceNotFoundException {
        if (StringUtils.isAnyBlank(request.getUserId(), request.getEventId())) {
            throw new InvalidFavoriteException("The request is invalid.");
        }

        Optional<Event> eventOptional = this.eventDao.findById(request.getEventId());
        if (eventOptional.isEmpty()) {
            throw new ResourceNotFoundException("The event not found  with id:" + request.getEventId());
        } else {
            Event event = eventOptional.get();
            Optional<Favorites> favoritesOptional = this.favoriteDao.findByUserIdAndEvent(request.getUserId(), event);

            if (favoritesOptional.isEmpty()) {
                throw new ResourceNotFoundException("The event is not on the favorite list of a user." + request.getUserId());
            } else {
                Favorites favorites = favoritesOptional.get();
                this.favoriteDao.delete(favorites);

                return getAllFavoriteDto(request.getUserId());
            }

        }
    }

}
