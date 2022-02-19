package com.achiever.menschenfahren.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.base.dto.request.FilterCreateDto;
import com.achiever.menschenfahren.dao.FilterEventDaoInterface;
import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.events.Event_;

@Repository
public class FilterEventDao implements FilterEventDaoInterface {

    @PersistenceContext
    EntityManager em;

    // @Autowired
    // private EventTypeDaoInterface eventTypeDao;

    @Override
    public List<Event> filterEvent(@Nonnull final FilterCreateDto request) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);

        Root<Event> event = cq.from(Event.class);
        cq.select(event);
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(request.getCountryId())) {
            Predicate hasCountryName = cb.equal(event.get(Event_.country), request.getCountryId());
            predicates.add(hasCountryName);
        }

        // create predicate for event type.
        // if (StringUtils.isNotBlank(request.getEventTypeId())) {
        // Optional<EventType> eventTypeOptional = eventTypeDao.findById(request.getEventTypeId());
        // if (eventTypeOptional.isPresent()) {
        // EventType eventType = eventTypeOptional.get();
        // Predicate hasEventType = cb.equal(event.get(Event_.eventType), eventType);
        // predicates.add(hasEventType);
        // }
        // }

        if (StringUtils.isNotBlank(request.getEventType())) {
            Predicate hasEventType = cb.equal(event.get(Event_.eventType), request.getEventType());
            predicates.add(hasEventType);
        }

        if (request.getFromDate() != null && request.getToDate() == null) {
            Path<Date> dateFromPath = event.get(Event_.startDate);
            predicates.add(cb.greaterThanOrEqualTo(dateFromPath, request.getFromDate()));
        } else if (request.getFromDate() != null && request.getToDate() != null) {
            Predicate date = cb.between(event.get(Event_.startDate), request.getFromDate(), request.getToDate());
            predicates.add(date);
        } else if (request.getFromDate() == null && request.getToDate() != null) {
            Path<Date> dateToPath = event.get(Event_.endDate);
            predicates.add(cb.lessThanOrEqualTo(dateToPath, request.getToDate()));
        }

        Predicate finalQuery = cb.or(predicates.toArray(new Predicate[] {}));

        cq.where(finalQuery);

        TypedQuery<Event> query = em.createQuery(cq);

        return query.getResultList();
    }

}
