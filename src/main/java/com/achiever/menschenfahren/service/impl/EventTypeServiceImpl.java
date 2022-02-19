package com.achiever.menschenfahren.service.impl;

import java.util.List;

import javax.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.achiever.menschenfahren.base.exception.InvalidEventTypeException;
import com.achiever.menschenfahren.dao.EventTypeDaoInterface;
import com.achiever.menschenfahren.entities.events.EventType;
import com.achiever.menschenfahren.service.EventTypeService;

@Service
public class EventTypeServiceImpl implements EventTypeService {

    @Autowired
    private EventTypeDaoInterface eventTypeDao;

    @Override
    public List<EventType> getEventTypes(boolean alsoVoided) {
        final List<EventType> eventTypes;
        if (alsoVoided) {
            eventTypes = eventTypeDao.findAll();
        } else {
            eventTypes = eventTypeDao.findByVoided(alsoVoided);
        }
        return eventTypes;
    }

    @Override
    @Nullable
    public EventType createEventType(EventType eventType) throws InvalidEventTypeException {
        final EventType eventTypeExists = eventTypeDao.findByName(eventType.getName());
        EventType savedEventType = null;
        if (eventTypeExists == null) {
            savedEventType = eventTypeDao.save(eventType);
        } else {
            throw new InvalidEventTypeException("Event Name must be unique.");
        }
        return savedEventType;
    }

}
