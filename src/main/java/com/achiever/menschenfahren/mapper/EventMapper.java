package com.achiever.menschenfahren.mapper;

import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.response.EventCreateDto;
import com.achiever.menschenfahren.entities.response.EventDto;
import com.achiever.menschenfahren.entities.response.EventEditDto;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class EventMapper extends ConfigurableMapper {

	@Override
	public void configure(final MapperFactory factory) {

		System.err.println("insidee mapperfactory");
		factory.classMap(Event.class, EventDto.class).byDefault().register();
		factory.classMap(Event.class, EventCreateDto.class).byDefault().register();
		factory.classMap(Event.class, EventEditDto.class).mapNullsInReverse(false).byDefault().register();
	}

}
