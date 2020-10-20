package com.achiever.menschenfahren.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.achiever.menschenfahren.dao.EventDaoInterface;
import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.service.EventService;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDaoInterface eventDao;

	/**
	 * Returns all the events based on the filter.
	 *
	 * @param alsoVoided Filters voided events.
	 * @return
	 */
	@Override
	public List<Event> getEvents(final boolean alsoVoided) {
		List<Event> events;
		if (alsoVoided) {
			events = eventDao.findAll();
		} else {
			events = eventDao.findByVoided(alsoVoided);
		}
		return events;
	}

	/**
	 * Returns event based on event id.
	 *
	 * @param eventId The id of the event.
	 * @return
	 */
	@Override
	public Optional<Event> getEvent(@Nonnull final String eventId, final boolean alsoVoided) {
		// return this.eventDao.findByEventIdAndVoided(eventId, alsoVoided);
		return null;
	}

	/**
	 * Create new Event.
	 *
	 * @param event The new event to create.
	 * @return
	 */
	@Override
	public Event createEvent(@Nonnull final Event event) {
		return this.eventDao.save(event);
	}

	/**
	 * Find events based on userId and voided parameter.
	 *
	 * @param userId     The identifier of an user.
	 * @param alsoVoided The identifier to check if the event is voided or not.
	 * @return {@link List} of {@link Event}
	 */
	@Override
	public List<Event> getEventsByUserId(@Nonnull final String userId, final boolean alsoVoided) {
		// final List<Event> events = eventDao.findByUserIdAndVoided(userId,
		// alsoVoided);
		return new ArrayList<>();
	}

}
