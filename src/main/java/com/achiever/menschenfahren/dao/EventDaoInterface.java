package com.achiever.menschenfahren.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.events.Event;

@Repository
public interface EventDaoInterface extends JpaRepository<Event, String> {

	/**
	 * Filters the {@link List} of {@link Event} based on voided flag.
	 *
	 * @param alsoVoided Flag to check if the event is voided or not.
	 * @return list of event.
	 */
	List<Event> findByVoided(final boolean alsoVoided);

	/**
	 * Find Event based on id, voided and isPrivate parameter.
	 *
	 * @param id          The identifier of the event.
	 * @param alsoVoided  Flag to check if the event is voided or not.
	 * @param alsoPrivate Flag to check for event is private or not.
	 * @return The event.
	 */
	Optional<Event> findByIdAndVoidedAndIsPrivate(@Nonnull final String id, final boolean alsoVoided,
			final boolean alsoPrivate);

	/**
	 * Finds list of events by voided and private flag.
	 * 
	 * @param alsoVoided  Filter for event to be voided or not.
	 * @param alsoPrivate Filter for event to be private or not.
	 * @return {@link List} of {@link Event}
	 */
	List<Event> findByVoidedAndIsPrivate(final boolean alsoVoided, final boolean alsoPrivate);

	/**
	 * Finds list of Event based on userId and voided flag.
	 * 
	 * @param userId     The identifier of the user.
	 * @param alsoVoided The flag for voided property.
	 * @return {@link List} of {@link Event}
	 */
	List<Event> findByUserIdAndVoided(@Nonnull final String userId, final boolean alsoVoided);

}
