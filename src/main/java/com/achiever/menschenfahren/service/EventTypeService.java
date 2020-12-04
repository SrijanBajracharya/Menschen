package com.achiever.menschenfahren.service;

import java.util.List;

import javax.annotation.Nonnull;

import com.achiever.menschenfahren.entities.events.EventType;
import com.achiever.menschenfahren.exception.InvalidEventTypeException;

public interface EventTypeService {

    List<EventType> getEventTypes(final boolean alsoVoided);

    EventType createEventType(@Nonnull final EventType eventType) throws InvalidEventTypeException;
}
