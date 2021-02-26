package com.achiever.menschenfahren.mapper;

import com.achiever.menschenfahren.base.dto.request.EventTypeCreateDto;
import com.achiever.menschenfahren.base.dto.request.EventTypeEditDto;
import com.achiever.menschenfahren.base.dto.response.EventTypeDto;
import com.achiever.menschenfahren.entities.events.EventType;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class EventTypeMapper extends ConfigurableMapper {

    @Override
    public void configure(final MapperFactory factory) {

        factory.classMap(EventType.class, EventTypeDto.class).byDefault().register();
        factory.classMap(EventType.class, EventTypeCreateDto.class).byDefault().register();
        factory.classMap(EventType.class, EventTypeEditDto.class).byDefault().register();
    }
}
