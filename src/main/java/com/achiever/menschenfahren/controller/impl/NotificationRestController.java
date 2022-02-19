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

import com.achiever.menschenfahren.base.controller.NotificationRestControllerInterface;
import com.achiever.menschenfahren.base.dto.request.NotificationCreateDto;
import com.achiever.menschenfahren.base.dto.request.NotificationEditDto;
import com.achiever.menschenfahren.base.dto.request.NotificationInviteDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.NotificationDto;
import com.achiever.menschenfahren.base.exception.InvalidNotificationException;
import com.achiever.menschenfahren.base.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.base.model.NotificationStatus;
import com.achiever.menschenfahren.base.model.NotificationType;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.notification.Notification;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.mapper.NotificationMapper;
import com.achiever.menschenfahren.service.EventService;
import com.achiever.menschenfahren.service.NotificationService;
import com.achiever.menschenfahren.service.impl.AuthenticationService;

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

    private final UserDaoInterface    userDao;

    private final NotificationMapper  notificationMapper;

    private final EventService        eventService;

    @Autowired
    private AuthenticationService     authenticationService;

    @Autowired
    public NotificationRestController(@Nonnull final NotificationService notificationService, @Nonnull final UserDaoInterface userDao,
            @Nonnull final EventService eventService) {
        this.notificationService = notificationService;
        this.userDao = userDao;
        this.eventService = eventService;
        this.notificationMapper = new NotificationMapper();

    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException
     * @throws InvalidNotificationException
     */
    @Override
    public ResponseEntity<DataResponse<NotificationDto>> createJoinRequest(@Nonnull @Valid final NotificationCreateDto request, final boolean alsoVoided)
            throws ResourceNotFoundException, InvalidNotificationException {
        String userId = authenticationService.getId();

        if (StringUtils.isAnyBlank(userId, request.getOriginalReceiverId(), request.getEventId())) {
            throw new InvalidNotificationException("The id of sender, receiver and event should be filled.");
        }

        Notification existedNotification = this.notificationService.findByOriginalSenderIdAndOriginalReceiverIdAndEventId(userId,
                request.getOriginalReceiverId(), request.getEventId());

        if (existedNotification != null && request.getNotificationType().equalsIgnoreCase(existedNotification.getNotificationType().getValue())) {
            log.info("The request has already been sent to the user.");
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

        final Notification savedNotification = createJoinNotification(request, alsoVoided);

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
            @Valid NotificationEditDto request) throws ResourceNotFoundException, InvalidNotificationException {

        // original receiver is taken from the token
        String originalReceiverId = authenticationService.getId();

        Optional<Notification> notificationOptional = this.notificationService.findById(notificationId);

        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();

            if (notification.getOriginalSender().getId().equals(originalSenderId) && notification.getOriginalReceiver().getId().equals(originalReceiverId)) {
                notification.setNotificationStatus(NotificationStatus.getByName(request.getNotificationStatus()));
                notification.setModifiedTimestamp(new Date());

                Notification savedNotification = this.notificationService.updateNotification(notification);

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
    public ResponseEntity<DataResponse<List<NotificationDto>>> getNotificationByToken(final boolean alsoVoided) throws InvalidNotificationException {
        String userId = authenticationService.getId();

        List<Notification> receivedNotification = this.notificationService.findByOriginalReceiverId(userId, alsoVoided);
        List<Notification> senderNotification = this.notificationService.findByOriginalSenderId(userId, alsoVoided);

        List<Notification> allNotification = new ArrayList<>();
        allNotification.addAll(receivedNotification);
        allNotification.addAll(senderNotification);

        List<NotificationDto> allNotificationDto = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(allNotification)) {
            for (Notification notification : allNotification) {
                NotificationDto convertedNotificationDto = this.notificationMapper.convertNotificationToNotificationDto(notification);
                checkReceverSenderUser(convertedNotificationDto, userId);
                allNotificationDto.add(convertedNotificationDto);
            }

            return buildResponse(allNotificationDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * Setting if the senderId matches or receiver Id matches with logged in user.
     *
     * @param notification
     *            The notification.e
     * @param userId
     *            The ID of user.
     */
    private void checkReceverSenderUser(@Nonnull final NotificationDto notification, @Nonnull final String userId) {

        if (notification.getSenderUser().getId().equals(userId)) {
            notification.setMatchedSenderUserId(true);
        } else if (notification.getReceiverUser().getId().equals(userId)) {
            notification.setMatchedReceiverUserId(true);
        }

    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidNotificationException
     * @throws ResourceNotFoundException
     */
    @Override
    public ResponseEntity<DataResponse<NotificationDto>> createInviteRequest(@Nonnull final NotificationInviteDto request)
            throws ResourceNotFoundException, InvalidNotificationException {
        String userId = authenticationService.getId();

        if (StringUtils.isAnyBlank(userId, request.getEventId(), request.getReceiverEmailId())) {
            throw new InvalidNotificationException("The id of sender, email id of receiver and event id should be filled.");
        }

        System.err.println(request.getEventId() + "##" + userId + "###senderId" + request.getReceiverEmailId() + "emailid");

        User receiverUser = this.userDao.findByEmail(request.getReceiverEmailId());

        if (receiverUser == null) {
            log.warn("The email address not found in our system." + request.getReceiverEmailId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Check if the invite has already been sent for the given user.
        Notification existedNotification = this.notificationService.findByOriginalSenderIdAndOriginalReceiverIdAndEventId(userId, receiverUser.getId(),
                request.getEventId());

        if (existedNotification != null && existedNotification.getNotificationType().equals(NotificationType.INVITE)) {
            log.info("The request has already been sent to the user.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {

            System.err.println("before create invite notification");
            Notification savedNotification = createInviteNotification(receiverUser, request);
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
     * @throws ResourceNotFoundException
     */
    private Notification createInviteNotification(@Nonnull final User receiverUser, @Nonnull final NotificationInviteDto request)
            throws InvalidNotificationException, ResourceNotFoundException {
        String userId = authenticationService.getId();

        Notification notification = new Notification();
        Optional<Event> savedEvent = this.eventService.findById(request.getEventId());
        if (savedEvent.isPresent()) {
            notification.setEvent(savedEvent.get());
        } else {
            throw new InvalidNotificationException("The Event not found");
        }
        notification.setNotificationType(NotificationType.INVITE);
        notification.setNotificationStatus(NotificationStatus.PENDING);

        Optional<User> OptionalSenderUser = this.userDao.findById(userId);
        if (OptionalSenderUser.isEmpty()) {
            throw new ResourceNotFoundException("The logged in user information not found");
        }

        User senderUser = OptionalSenderUser.get();
        notification.setOriginalSender(senderUser);
        notification.setOriginalReceiver(receiverUser);

        senderUser.getFriends().add(receiverUser.getId());
        receiverUser.getFriends().add(userId);

        notification.setVoided(false);

        System.err.println(senderUser + "###senderUser####");
        System.err.println(receiverUser + "###receiverUser###");
        System.err.println(notification + "###notification####");
        return this.notificationService.createNotification(notification, senderUser, receiverUser);

    }

    private Notification createJoinNotification(@Nonnull final NotificationCreateDto request, final boolean alsoVoided)
            throws InvalidNotificationException, ResourceNotFoundException {
        String userId = authenticationService.getId();

        Notification notification = new Notification();
        Optional<Event> savedEvent = this.eventService.findById(request.getEventId());
        if (savedEvent.isPresent()) {
            notification.setEvent(savedEvent.get());
        } else {
            throw new InvalidNotificationException("The Event not found");
        }
        notification.setNotificationType(NotificationType.fromString(request.getNotificationType()));
        notification.setNotificationStatus(NotificationStatus.getByName(request.getNotificationStatus()));

        Optional<User> optionalSenderUser = this.userDao.findById(userId);
        if (optionalSenderUser.isEmpty()) {
            throw new ResourceNotFoundException("The logged in user information not found with id: " + userId);
        }

        Optional<User> optionalReceiverUser = this.userDao.findById(request.getOriginalReceiverId());

        if (optionalReceiverUser.isEmpty()) {
            throw new ResourceNotFoundException("The receiver user information not found with id: " + request.getOriginalReceiverId());
        }

        User senderUser = optionalSenderUser.get();
        User receiverUser = optionalReceiverUser.get();
        notification.setOriginalSender(senderUser);
        notification.setOriginalReceiver(receiverUser);

        senderUser.getFriends().add(request.getOriginalReceiverId());
        receiverUser.getFriends().add(userId);

        notification.setVoided(false);
        return this.notificationService.createNotification(notification, senderUser, receiverUser);
    }

}
