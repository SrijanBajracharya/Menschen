package com.achiever.menschenfahren.mapper;

import javax.annotation.Nonnull;

import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.response.EventCreateDto;
import com.achiever.menschenfahren.entities.response.EventDto;
import com.achiever.menschenfahren.entities.response.EventEditDto;
import com.achiever.menschenfahren.entities.users.User;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class EventMapper extends ConfigurableMapper {

    @Override
    public void configure(final MapperFactory factory) {

        factory.classMap(Event.class, EventDto.class).byDefault().register();
        factory.classMap(Event.class, EventCreateDto.class).byDefault().register();
        factory.classMap(Event.class, EventEditDto.class).mapNullsInReverse(false).byDefault().register();
    }

    public Event convertEventCreateDtoToEvent(@Nonnull final EventCreateDto request, @Nonnull final User user) {
        final Event event = new Event();
        event.setAgeGroup(request.getAgeGroup());
        event.setCountryCode(request.getCountryCode());
        event.setDescription(request.getDescription());
        event.setEndDate(request.getEndDate());
        event.setEventType(request.getEventType());
        event.setGallery(request.getGallery());
        event.setLocation(request.getLocation());
        event.setName(request.getName());
        event.setNumberOfParticipants(request.getNumberOfParticipants());
        event.setStartDate(request.getStartDate());
        event.setUser(user);
        return event;
    }

    public EventDto convertEventToEventDto(@Nonnull final Event event) {
        final EventDto eventDto = new EventDto();
        eventDto.setAgeGroup(event.getAgeGroup());
        eventDto.setCountryCode(event.getCountryCode());
        eventDto.setDescription(event.getDescription());
        eventDto.setEndDate(event.getEndDate());
        eventDto.setEventType(event.getEventType());
        eventDto.setGallery(event.getGallery());
        eventDto.setLocation(event.getLocation());
        eventDto.setName(event.getName());
        eventDto.setNumberOfParticipants(event.getNumberOfParticipants());
        eventDto.setStartDate(event.getStartDate());
        eventDto.setUserId(event.getUser().getId());
        eventDto.setId(event.getId());
        return eventDto;
    }

}
