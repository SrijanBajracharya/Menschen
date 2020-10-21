package com.achiever.menschenfahren.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.achiever.menschenfahren.entities.events.Event;

public interface EventService {

	/**
	 * Returns all the events based on the filter.
	 *
	 * @param alsoVoided Filters voided events.
	 * @return
	 */
	List<Event> getEvents(final boolean alsoVoided, final boolean alsoPrivate);

	/**
	 * Returns event based on event id.
	 *
	 * @param eventId The id of the event.
	 * @return
	 */
	Optional<Event> getEvent(@Nonnull final String eventId, final boolean alsoVoided, final boolean alsoPrivate);

	/**
	 * Create new Event.
	 *
	 * @param event The new event to create.
	 * @return
	 */
	Event createEvent(@Nonnull final Event event);

	/**
	 * Find events based on userId and voided parameter.
	 *
	 * @param userId     The identifier of an user.
	 * @param alsoVoided The identifier to check if the event is voided or not.
	 * @return {@link List} of {@link Event}
	 */
	List<Event> getEventsByUserId(@Nonnull final String userId, final boolean alsoVoided);

}
