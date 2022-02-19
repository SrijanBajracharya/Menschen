package com.achiever.menschenfahren.controller.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.base.controller.EventRestControllerInterface;
import com.achiever.menschenfahren.base.dto.request.EventCreateDto;
import com.achiever.menschenfahren.base.dto.request.EventEditDto;
import com.achiever.menschenfahren.base.dto.request.FilterCreateDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.EventDto;
import com.achiever.menschenfahren.base.exception.InvalidEventException;
import com.achiever.menschenfahren.base.exception.InvalidEventTypeException;
import com.achiever.menschenfahren.base.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.base.model.NotificationStatus;
import com.achiever.menschenfahren.base.model.NotificationType;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.dao.EventTypeDaoInterface;
import com.achiever.menschenfahren.dao.FavoritesDaoInterface;
import com.achiever.menschenfahren.dao.FilterEventDaoInterface;
import com.achiever.menschenfahren.dao.NotificationDaoInterface;
import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.events.EventType;
import com.achiever.menschenfahren.entities.events.Favorites;
import com.achiever.menschenfahren.entities.notification.Notification;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.mapper.EventMapper;
import com.achiever.menschenfahren.service.EventService;
import com.achiever.menschenfahren.service.impl.AuthenticationService;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Srijan Bajracharya
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
@Slf4j
public class EventRestController extends BaseController implements EventRestControllerInterface {

    @Autowired
    private EventService             eventService;

    @Autowired
    private UserDaoInterface         userDao;

    @Autowired
    private FilterEventDaoInterface  filterEventDao;

    @Autowired
    private AuthenticationService    authenticationService;

    @Autowired
    private EventTypeDaoInterface    eventTypeDao;

    @Autowired
    private NotificationDaoInterface notificationDao;

    @Autowired
    private FavoritesDaoInterface    favoriteDao;

    private final EventMapper        eventMapper = new EventMapper();

    /**
     * Get all events based on alsoVoided and alsoPrivate filter.
     */
    @Override
    public ResponseEntity<DataResponse<List<EventDto>>> getEvents(final boolean alsoVoided, final boolean alsoPrivate) {
        final List<Event> events = this.eventService.getEvents(alsoVoided, alsoPrivate);

        List<Event> filteredEvents = events.stream().filter(event -> event.getEndDate().after(new Date())).collect(Collectors.toList());

        String userId = authenticationService.getId();
        final List<EventDto> eventDtoList = new ArrayList<>();
        for (final Event event : filteredEvents) {
            final EventDto eventDto = this.eventMapper.map(event, EventDto.class);
            eventDto.setEventTypeId(event.getEventType().getId());
            eventDto.setUserId(event.getUser().getId());
            if (StringUtils.isNotBlank(userId)) {
                getFavoritesByUser(userId, eventDto);
            }
            eventDtoList.add(eventDto);
        }

        if (eventDtoList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return buildResponse(eventDtoList, HttpStatus.OK);
        }

    }

    /**
     * Get event based on the eventId and using alsoVoided filter.
     */
    @Override
    public ResponseEntity<DataResponse<EventDto>> getEvent(@Nonnull final String eventId, final boolean alsoVoided, final boolean alsoPrivate) {
        final Optional<Event> eventOptional = this.eventService.getEvent(eventId, alsoVoided, alsoPrivate);
        String userId = authenticationService.getId();
        if (!eventOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            final Event event = eventOptional.get();
            final EventDto eventDto = this.eventMapper.map(event, EventDto.class);
            if (eventDto != null) {
                eventDto.setUserId(event.getUser().getId());
                eventDto.setEventTypeId(event.getEventType().getId());
                if (StringUtils.isNotBlank(userId)) {
                    getFavoritesByUser(userId, eventDto);
                }
                return buildResponse(eventDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.GONE);
            }
        }
    }

    /**
     * creates Event.
     *
     * @throws InvalidEventTypeException
     */
    @Override
    public ResponseEntity<DataResponse<EventDto>> createEvent(@Nonnull @Valid final EventCreateDto request)
            throws InvalidEventException, InvalidEventTypeException {
        String userId = authenticationService.getId();

        final Optional<User> user = this.userDao.findById(userId);

        if (user.isPresent()) {
            final User foundUser = user.get();

            Optional<EventType> eventTypeOptional = eventTypeDao.findById(request.getEventTypeId());
            if (eventTypeOptional.isEmpty()) {
                throw new InvalidEventTypeException("Event type not found with id:" + request.getEventTypeId());
            } else {
                EventType foundEventType = eventTypeOptional.get();
                final Event event = this.eventMapper.map(request, Event.class);
                event.setUser(foundUser);
                event.setEventType(foundEventType);
                final Event savedEvent = this.eventService.createEvent(event);

                final EventDto eventDto = this.eventMapper.map(savedEvent, EventDto.class);
                if (eventDto != null) {
                    eventDto.setUserId(foundUser.getId());
                    eventDto.setEventTypeId(request.getEventTypeId());
                }
                if (eventDto != null) {
                    return buildResponse(eventDto, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }

        } else {
            log.warn("User not found with id:" + request.getUserId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Gets all the events based on token
     */
    @Override
    public ResponseEntity<DataResponse<List<EventDto>>> getUserEvents(final boolean alsoVoided) throws InvalidEventException {
        String userId = authenticationService.getId();
        final List<Event> events = this.eventService.getEventsByUserId(userId, alsoVoided);

        // Finds the event Ids which was approved for your request or you have accepted the invite.
        Set<String> eventIds = findApprovedInviteOrRequestEventIds(userId);

        if (!CollectionUtils.isEmpty(eventIds)) {
            List<Event> joinedEvent = this.eventService.getEventByIds(eventIds);

            events.addAll(joinedEvent);
        }

        final List<EventDto> myEvents = new ArrayList<>();

        for (final Event event : events) {
            final EventDto eventDto = this.eventMapper.map(event, EventDto.class);
            eventDto.setUserId(userId);
            getFavoritesByUser(userId, eventDto);
            eventDto.setEventTypeId(event.getEventType().getId());
            myEvents.add(eventDto);
        }

        if (myEvents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return buildResponse(myEvents, HttpStatus.OK);
        }
    }

    /**
     * Makes event private based on the eventId.
     */
    @Override
    public ResponseEntity<DataResponse<EventDto>> makeEventPrivate(@Nonnull final String eventId) throws ResourceNotFoundException {

        String userId = authenticationService.getId();
        final Optional<User> user = this.userDao.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with Id:" + userId + " not found");
        }

        final Event foundEvent = getEventById(eventId);
        foundEvent.setPrivate(true);
        final Event savedEvent = this.eventService.merge(foundEvent);
        final EventDto response = eventMapper.map(savedEvent, EventDto.class);
        // TODO: This is not the nice way to set the values. Need to find a better way to do this.
        response.setEventTypeId(savedEvent.getEventType().getId());
        response.setUserId(savedEvent.getUser().getId());
        getFavoritesByUser(userId, response);
        return buildResponse(response, HttpStatus.OK);

    }

    /**
     * Makes Event public based on the eventId.
     *
     * @throws ResourceNotFoundException
     */
    @Override
    public ResponseEntity<DataResponse<EventDto>> makeEventPublic(@Nonnull final String eventId) throws ResourceNotFoundException {
        String userId = authenticationService.getId();
        final Optional<User> user = this.userDao.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with Id:" + userId + " not found");
        }

        final Event event = getEventById(eventId);

        event.setPrivate(false);
        final Event savedEvent = this.eventService.merge(event);
        EventDto eventDto = eventMapper.map(savedEvent, EventDto.class);
        getFavoritesByUser(userId, eventDto);
        eventDto.setUserId(savedEvent.getUser().getId());
        eventDto.setEventTypeId(savedEvent.getEventType().getId());
        return buildResponse(eventDto, HttpStatus.OK);

    }

    /**
     * Update event based on the eventId and provided set of updated values.
     */
    @Override
    public ResponseEntity<DataResponse<EventDto>> editEvent(@Nonnull final String eventId, @Valid final EventEditDto request) throws ResourceNotFoundException {
        String userId = authenticationService.getId();
        final Optional<User> user = this.userDao.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with Id:" + userId + " not found");
        }
        final Event event = getEventById(eventId);
        if (event == null) {
            throw new ResourceNotFoundException("The event not found with id: " + eventId);
        }
        Optional<EventType> eventTypeOptional = eventTypeDao.findById(request.getEventTypeId());
        if (eventTypeOptional.isEmpty()) {
            throw new ResourceNotFoundException("The event type id not found." + request.getEventTypeId());
        }
        eventMapper.map(request, event);
        event.setEventType(eventTypeOptional.get());

        final Event savedEvent = eventService.createEvent(event);
        final EventDto response = eventMapper.map(savedEvent, EventDto.class);
        getFavoritesByUser(userId, response);
        response.setUserId(savedEvent.getUser().getId());
        response.setEventTypeId(savedEvent.getEventType().getId());
        return buildResponse(response, HttpStatus.OK);
    }

    /**
     * Returns the event or throws an exception if the event is not present or disallowed to edit.
     *
     * @param eventId
     *            The id of event.
     * @return The found event.
     * @throws ResourceNotFoundException
     *             If no event existed.
     */
    @Nonnull
    private Event getEventById(@Nonnull final String eventId) throws ResourceNotFoundException {
        final Optional<Event> eventOptional = this.eventService.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new ResourceNotFoundException("No Event found with id:" + eventId);
        }
        final Event event = eventOptional.get();
        return event;
    }

    /**
     * Filters event based on the filter request.
     */
    @Override
    public ResponseEntity<DataResponse<List<EventDto>>> filterEvent(@Nonnull final @Valid FilterCreateDto request) {

        List<Event> filteredEvents = this.filterEventDao.filterEvent(request);

        if (!filteredEvents.isEmpty()) {
            final List<EventDto> events = new ArrayList<>();

            for (final Event event : filteredEvents) {
                final EventDto eventDto = this.eventMapper.map(event, EventDto.class);
                eventDto.setUserId(event.getUser().getId());
                eventDto.setEventTypeId(event.getEventType().getId());
                events.add(eventDto);
            }

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return buildResponse(events, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * Set events which user has marked favorite.
     *
     * @param userId
     *            The ID of user.
     * @param eventDto
     *            The Event dto.
     */
    private void getFavoritesByUser(@Nonnull final String userId, @Nonnull final EventDto eventDto) {
        List<Favorites> favorites = this.favoriteDao.findByUserId(userId);
        for (Favorites fav : favorites) {
            if (fav.getEvent().getId().equals(eventDto.getId())) {
                eventDto.setFavorite(true);
            }
        }
    }

    /**
     * Finds list of notification of a user and filters only approved request or invite.
     *
     * @param userId
     *            The ID of user.
     * @return The list of approved event IDs.
     */
    @Nonnull
    private Set<String> findApprovedInviteOrRequestEventIds(@Nonnull final String userId) {
        // find REQUEST Event which is approved by the Event owner or . The userId in this case is the original sender -> REQUEST
        List<Notification> notifications = this.notificationDao.findByOriginalSenderIdOrOriginalReceiverId(userId, userId);
        List<Notification> filteredNotifications = notifications.stream().filter(notification -> (notification.getNotificationType() == NotificationType.INVITE
                && notification.getNotificationStatus() == NotificationStatus.APPROVED)
                || (notification.getNotificationType() == NotificationType.REQUEST && notification.getNotificationStatus() == NotificationStatus.APPROVED))
                .collect(Collectors.toList());
        Set<String> eventIds = filteredNotifications.stream().map(f -> f.getEvent().getId()).collect(Collectors.toSet());
        return eventIds;
    }
}
