package com.achiever.menschenfahren.dao;

import java.util.List;

import javax.annotation.Nonnull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.events.EventType;

@Repository
public interface EventTypeDaoInterface extends JpaRepository<EventType, String> {

    List<EventType> findByVoided(final boolean alsoVoided);

    EventType findByName(@Nonnull final String name);
}
