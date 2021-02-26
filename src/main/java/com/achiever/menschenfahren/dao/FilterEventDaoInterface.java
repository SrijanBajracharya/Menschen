package com.achiever.menschenfahren.dao;

import java.util.List;

import javax.annotation.Nonnull;

import com.achiever.menschenfahren.base.dto.request.FilterCreateDto;
import com.achiever.menschenfahren.entities.events.Event;

public interface FilterEventDaoInterface {

    List<Event> filterEvent(@Nonnull final FilterCreateDto request);

}
