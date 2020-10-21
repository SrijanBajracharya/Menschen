package com.achiever.menschenfahren.controller.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.controller.EventRestControllerInterface;
import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.response.DataResponse;
import com.achiever.menschenfahren.entities.response.EventCreateDto;
import com.achiever.menschenfahren.entities.response.EventDto;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.exception.InvalidEventException;
import com.achiever.menschenfahren.mapper.EventMapper;
import com.achiever.menschenfahren.service.EventService;
import com.achiever.menschenfahren.service.UserService;

/**
 *
 * @author Srijan Bajracharya
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
public class EventRestController extends BaseController implements EventRestControllerInterface {

	@Autowired
	private EventService eventService;

	@Autowired
	private UserService userService;

	private final EventMapper eventMapper = new EventMapper();

	public EventRestController() {
		super();
	}

	@Override
	public ResponseEntity<DataResponse<List<EventDto>>> getEvents(final boolean alsoVoided, final boolean alsoPrivate) {
		final List<Event> events = this.eventService.getEvents(alsoVoided, alsoPrivate);

		List<EventDto> eventDtoList = new ArrayList<>();
		for (final Event event : events) {
			eventDtoList.add(this.eventMapper.convertEventToEventDto(event));
		}

		if (eventDtoList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return buildResponse(eventDtoList, HttpStatus.OK);
		}

	}

	@Override
	public ResponseEntity<DataResponse<EventDto>> getEvent(@Nonnull final String eventId, final boolean alsoVoided,
			final boolean alsoPrivate) {
		final Optional<Event> eventOptional = this.eventService.getEvent(eventId, alsoVoided, alsoPrivate);
		if (!eventOptional.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			final Event event = eventOptional.get();
			final EventDto eventDto = this.eventMapper.convertEventToEventDto(event);
			if (eventDto != null) {
				return buildResponse(eventDto, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.GONE);
			}
		}
	}

	@Override
	public ResponseEntity<DataResponse<EventDto>> createEvent(@Nonnull @Valid final EventCreateDto request)
			throws InvalidEventException {

//		final Event event = this.eventMapper.map(request, Event.class);
//		System.err.println(event + "####user");
//		final Event savedEvent = this.eventService.createEvent(event);
//
//		final EventDto savedEventDto = this.eventMapper.map(savedEvent, EventDto.class);
//		System.err.println(savedEventDto + "####userDto saved");

		Optional<User> user = this.userService.findById(request.getUserId());
		if (user.isPresent()) {
			User foundUser = user.get();
			final Event event = this.eventMapper.convertEventCreateDtoToEvent(request, foundUser);
			final Event savedEvent = this.eventService.createEvent(event);

			final EventDto eventDto = this.eventMapper.convertEventToEventDto(savedEvent);
			if (eventDto != null) {
				return buildResponse(eventDto, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public ResponseEntity<DataResponse<List<EventDto>>> getEventsByUserId(@Nonnull final String userId,
			final boolean alsoVoided) throws InvalidEventException {
		final List<Event> events = this.eventService.getEventsByUserId(userId, alsoVoided);

		final List<EventDto> myEvents = new ArrayList<>();

		for (Event event : events) {
			myEvents.add(this.eventMapper.convertEventToEventDto(event));
		}

		if (myEvents.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return buildResponse(myEvents, HttpStatus.OK);
		}
	}

}
