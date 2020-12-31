package com.achiever.menschenfahren.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.events.Favorites;

@Repository
public interface FavoritesDaoInterface extends JpaRepository<Favorites, String> {

    Optional<Favorites> findByUserIdAndEvent(@Nonnull final String userId, @Nonnull final Event event);

    List<Favorites> findByUserId(@Nonnull final String userId);
}
