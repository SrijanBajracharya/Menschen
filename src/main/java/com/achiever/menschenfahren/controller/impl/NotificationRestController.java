package com.achiever.menschenfahren.controller.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.base.dto.request.NotificationCreateDto;
import com.achiever.menschenfahren.base.dto.request.NotificationEditDto;
import com.achiever.menschenfahren.base.dto.request.NotificationInviteDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.NotificationDto;
import com.achiever.menschenfahren.base.model.NotificationStatus;
import com.achiever.menschenfahren.base.model.NotificationType;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.controller.NotificationRestControllerInterface;
import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.notification.Notification;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.exception.InvalidNotificationException;
import com.achiever.menschenfahren.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.mapper.NotificationMapper;
import com.achiever.menschenfahren.service.EventService;
import com.achiever.menschenfahren.service.NotificationService;
import com.achiever.menschenfahren.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * The controller for handling all notification related functionality.
 *
 * @author Srijan Bajracharya
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
@Slf4j
public class NotificationRestController extends BaseController implements NotificationRestControllerInterface {

    private final NotificationService notificationService;

    private final UserService         userService;

    private final NotificationMapper  notificationMapper;

    private final EventService        eventService;

    @Autowired
    public NotificationRestController(@Nonnull final NotificationService notificationService, @Nonnull final UserService userService,
            @Nonnull final EventService eventService) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.eventService = eventService;
        this.notificationMapper = new NotificationMapper();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<NotificationDto>> createJoinRequest(@Nonnull @Valid final NotificationCreateDto request, final boolean alsoVoided)
            throws InvalidNotificationException {

        if (StringUtils.isAnyBlank(request.getOriginalSenderId(), request.getOriginalReceiverId(), request.getEventId())) {
            throw new InvalidNotificationException("The id of sender, receiver and event should be filled.");
        }

        Notification existedNotification = this.notificationService.findByOriginalSenderIdAndOriginalReceiverIdAndEventId(request.getOriginalSenderId(),
                request.getOriginalReceiverId(), request.getEventId());

        if (existedNotification != null && request.getNotificationType().equalsIgnoreCase(existedNotification.getNotificationType().getValue())) {
            log.info("The request has already been sent to the user.");
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

        final Notification notification = createJoinNotification(request, alsoVoided);

        Notification savedNotification = this.notificationService.createNotification(notification);

        if (savedNotification != null) {
            NotificationDto notificationDto = this.notificationMapper.convertNotificationToNotificationDto(savedNotification);
            return buildResponse(notificationDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<NotificationDto>> updateNotification(@Nonnull final String notificationId, @Nonnull final String originalSenderId,
            @Nonnull final String originalRecieverId, @Valid NotificationEditDto request) throws ResourceNotFoundException, InvalidNotificationException {

        Optional<Notification> notificationOptional = this.notificationService.findById(notificationId);

        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();

            if (notification.getOriginalSender().getId().equals(originalSenderId) && notification.getOriginalReceiver().getId().equals(originalRecieverId)) {
                notification.setNotificationStatus(NotificationStatus.getByName(request.getNotificationStatus()));
                notification.setModifiedTimestamp(new Date());

                Notification savedNotification = this.notificationService.createNotification(notification);

                if (savedNotification != null) {
                    NotificationDto notificationDto = this.notificationMapper.convertNotificationToNotificationDto(savedNotification);
                    return buildResponse(notificationDto, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            } else {

                throw new InvalidNotificationException("The information is incorrect, please check if the receiver and sender id are correct.");

            }
        } else {
            throw new ResourceNotFoundException("The notification not found with notification id:" + notificationId);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<List<NotificationDto>>> getNotificationsByUserId(@Nonnull final String userId, final boolean alsoVoided)
            throws InvalidNotificationException {
        List<Notification> receivedNotification = this.notificationService.findByOriginalReceiverId(userId, alsoVoided);
        List<Notification> senderNotification = this.notificationService.findByOriginalSenderId(userId, alsoVoided);

        List<Notification> allNotification = new ArrayList<>();
        allNotification.addAll(receivedNotification);
        allNotification.addAll(senderNotification);

        List<NotificationDto> allNotificationDto = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(allNotification)) {
            for (Notification notification : allNotification) {
                NotificationDto convertedNotificationDto = this.notificationMapper.convertNotificationToNotificationDto(notification);
                allNotificationDto.add(convertedNotificationDto);
            }

            return buildResponse(allNotificationDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidNotificationException
     */
    @Override
    public ResponseEntity<DataResponse<NotificationDto>> createInviteRequest(@Nonnull final NotificationInviteDto request) throws InvalidNotificationException {

        if (StringUtils.isAnyBlank(request.getOriginalSenderId(), request.getEventId(), request.getReceiverEmailId())) {
            throw new InvalidNotificationException("The id of sender, email id of receiver and event id should be filled.");
        }

        User user = this.userService.findByEmail(request.getReceiverEmailId());

        if (user == null) {
            log.warn("The email address not found in our system.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Check if the invite has already been sent for the given user.

        Notification existedNotification = this.notificationService.findByOriginalSenderIdAndOriginalReceiverIdAndEventId(request.getOriginalSenderId(),
                user.getId(), request.getEventId());

        if (existedNotification != null && existedNotification.getNotificationType().equals(NotificationType.INVITE)) {
            log.info("The request has already been sent to the user.");
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        } else {

            Notification savedNotification = createInviteNotification(user, request);
            if (savedNotification != null) {
                NotificationDto notificationDto = this.notificationMapper.convertNotificationToNotificationDto(savedNotification);
                log.info("The notification has been created.");
                return buildResponse(notificationDto, HttpStatus.CREATED);
            } else {
                log.error("Couldn't save the notification.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        }

    }

    /**
     * Create invite notification.
     *
     * @param user
     *            The user to whom invite is sent.
     * @param request
     *            The request object to sent the invitation
     * @return The created notification.
     * @throws InvalidNotificationException
     */
    private Notification createInviteNotification(@Nonnull final User user, @Nonnull final NotificationInviteDto request) throws InvalidNotificationException {
        Notification notification = new Notification();
        Optional<Event> savedEvent = this.eventService.findById(request.getEventId());
        if (savedEvent.isPresent()) {
            notification.setEvent(savedEvent.get());
        } else {
            throw new InvalidNotificationException("The Event not found");
        }
        notification.setNotificationType(NotificationType.INVITE);
        notification.setNotificationStatus(NotificationStatus.PENDING);

        Optional<User> senderUser = this.userService.findById(request.getOriginalSenderId());
        if (senderUser.isPresent()) {
            notification.setOriginalSender(senderUser.get());
        } else {
            throw new InvalidNotificationException("The logged in user information not found");
        }

        notification.setOriginalReceiver(user);
        notification.setVoided(false);
        return this.notificationService.createNotification(notification);

    }

    private Notification createJoinNotification(@Nonnull final NotificationCreateDto request, final boolean alsoVoided) throws InvalidNotificationException {
        Notification notification = new Notification();
        Optional<Event> savedEvent = this.eventService.findById(request.getEventId());
        if (savedEvent.isPresent()) {
            notification.setEvent(savedEvent.get());
        } else {
            throw new InvalidNotificationException("The Event not found");
        }
        notification.setNotificationType(NotificationType.fromString(request.getNotificationType()));
        notification.setNotificationStatus(NotificationStatus.getByName(request.getNotificationStatus()));

        Optional<User> senderUser = this.userService.findById(request.getOriginalSenderId());
        if (senderUser.isPresent()) {
            notification.setOriginalSender(senderUser.get());
        } else {
            throw new InvalidNotificationException("The logged in user information not found");
        }

        Optional<User> receiverUser = this.userService.findById(request.getOriginalReceiverId());
        if (receiverUser.isPresent()) {
            notification.setOriginalReceiver(receiverUser.get());
        } else {
            throw new InvalidNotificationException("The receiver user information not found");
        }

        notification.setVoided(false);
        return this.notificationService.createNotification(notification);
    }

}
