package com.achiever.menschenfahren.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.response.DataResponse;
import com.achiever.menschenfahren.entities.response.EventCreateDto;
import com.achiever.menschenfahren.exception.InvalidEventException;
import com.achiever.menschenfahren.service.EventService;

/**
 *
 * @author Srijan Bajracharya
 *
 */
@RestController
@RequestMapping("/api")
public class EventRestController extends BaseController implements EventRestControllerInterface {

	@Autowired
	private EventService eventService;

	// private final EventMapper eventMapper = new EventMapper();

	@Override
	public ResponseEntity<DataResponse<List<Event>>> getEvents(final boolean alsoVoided) {
		final List<Event> events = this.eventService.getEvents(alsoVoided);

		if (events.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return buildResponse(events, HttpStatus.OK);
		}

	}

	@Override
	public ResponseEntity<DataResponse<Event>> getEvent(@Nonnull final String eventId, final boolean alsoVoided) {
		final Optional<Event> eventOptional = this.eventService.getEvent(eventId, alsoVoided);
		if (!eventOptional.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			final Event event = eventOptional.get();
			if (!alsoVoided && event.isVoided()) {
				return new ResponseEntity<>(HttpStatus.GONE);
			} else {
				return buildResponse(event, HttpStatus.OK);
			}
		}
	}

	@Override
	public ResponseEntity<DataResponse<Event>> createEvent(@Nonnull @Valid final EventCreateDto request)
			throws InvalidEventException {
//		final Event event = this.eventMapper.map(request, Event.class);
//		final Event savedEvent = this.eventService.createEvent(event);
//
//		return buildResponse(savedEvent, HttpStatus.CREATED);
		return null;
	}

	@Override
	public ResponseEntity<DataResponse<List<Event>>> getEventsByUserId(@Nonnull final String userId,
			final boolean alsoVoided) throws InvalidEventException {
		final List<Event> events = this.eventService.getEventsByUserId(userId, alsoVoided);

		if (events.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return buildResponse(events, HttpStatus.OK);
		}
	}

}
