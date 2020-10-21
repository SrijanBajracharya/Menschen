package com.achiever.menschenfahren.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.events.EventType;

@Repository
public interface EventTypeDaoInterface extends JpaRepository<EventType, String> {

}
