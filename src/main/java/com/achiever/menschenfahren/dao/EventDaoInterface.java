package com.achiever.menschenfahren.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.events.Event;

@Repository
public interface EventDaoInterface extends JpaRepository<Event, String> {

	List<Event> findByVoided(final boolean alsoVoided);
//
//	List<Event> findByUserIdAndVoided(@Nonnull final String userId, final boolean alsoVoided);
//
//	Optional<Event> findByEventIdAndVoided(@Nonnull final String eventId, final boolean alsoVoided);

}
