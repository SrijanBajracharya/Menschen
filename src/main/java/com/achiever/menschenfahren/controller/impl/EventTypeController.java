package com.achiever.menschenfahren.controller.impl;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.base.controller.EventTypeControllerInterface;
import com.achiever.menschenfahren.base.dto.request.EventTypeCreateDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.EventTypeDto;
import com.achiever.menschenfahren.base.exception.InvalidEventTypeException;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.entities.events.EventType;
import com.achiever.menschenfahren.mapper.EventTypeMapper;
import com.achiever.menschenfahren.service.EventTypeService;

/****
 * 
 * @author Srijan Bajracharya
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
public class EventTypeController extends BaseController implements EventTypeControllerInterface {

    private final EventTypeMapper eventTypeMapper;

    @Autowired
    private EventTypeService      eventTypeService;

    public EventTypeController() {
        super();
        this.eventTypeMapper = new EventTypeMapper();
    }

    @Override
    public ResponseEntity<DataResponse<List<EventTypeDto>>> getEventTypes(boolean alsoVoided) {
        final List<EventType> eventTypes = this.eventTypeService.getEventTypes(alsoVoided);
        System.err.println();

        final List<EventTypeDto> eventTypeDtoList = new ArrayList<>();

        for (final EventType eventType : eventTypes) {
            eventTypeDtoList.add(this.eventTypeMapper.map(eventType, EventTypeDto.class));
        }

        if (eventTypeDtoList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return buildResponse(eventTypeDtoList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<DataResponse<EventTypeDto>> createEventType(@Valid EventTypeCreateDto request, boolean alsoVoided) throws InvalidEventTypeException {
        final EventType eventType = this.eventTypeMapper.map(request, EventType.class);
        final EventType savedEventType = this.eventTypeService.createEventType(eventType);

        final EventTypeDto savedEventTypeDto = this.eventTypeMapper.map(savedEventType, EventTypeDto.class);

        if (savedEventTypeDto != null) {
            return buildResponse(savedEventTypeDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
