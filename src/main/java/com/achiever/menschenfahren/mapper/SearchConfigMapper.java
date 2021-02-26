package com.achiever.menschenfahren.mapper;

import com.achiever.menschenfahren.base.dto.request.SearchConfigCreateDto;
import com.achiever.menschenfahren.base.dto.request.SearchConfigEditDto;
import com.achiever.menschenfahren.base.dto.response.SearchConfigResponseDto;
import com.achiever.menschenfahren.entities.configuration.SearchConfiguration;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class SearchConfigMapper extends ConfigurableMapper {

    @Override
    public void configure(final MapperFactory factory) {

        factory.classMap(SearchConfiguration.class, SearchConfigResponseDto.class).byDefault().register();
        factory.classMap(SearchConfiguration.class, SearchConfigCreateDto.class).byDefault().register();
        factory.classMap(SearchConfiguration.class, SearchConfigEditDto.class).mapNullsInReverse(false).byDefault().register();
    }
}
