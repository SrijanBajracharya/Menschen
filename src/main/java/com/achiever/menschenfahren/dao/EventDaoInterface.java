package com.achiever.menschenfahren.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.events.Event;

@Repository
public interface EventDaoInterface extends JpaRepository<Event, String> {

	List<Event> findByVoided(final boolean alsoVoided);

	Optional<Event> findByIdAndVoidedAndIsPrivate(@Nonnull final String userId, final boolean alsoVoided,
			final boolean alsoPrivate);

	List<Event> findByVoidedAndIsPrivate(final boolean alsoVoided, final boolean alsoPrivate);

}
