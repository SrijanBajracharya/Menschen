package com.achiever.menschenfahren.mapper;

import com.achiever.menschenfahren.base.dto.request.FavoriteCreateDto;
import com.achiever.menschenfahren.base.dto.response.AllFavoritesResponse;
import com.achiever.menschenfahren.base.dto.response.FavoritesDto;
import com.achiever.menschenfahren.entities.events.Favorites;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class FavoritesMapper extends ConfigurableMapper {

    @Override
    public void configure(final MapperFactory factory) {

        factory.classMap(Favorites.class, FavoritesDto.class).byDefault().register();
        factory.classMap(Favorites.class, FavoriteCreateDto.class).byDefault().register();
        factory.classMap(Favorites.class, AllFavoritesResponse.class).byDefault().register();
    }
}