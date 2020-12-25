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
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.NotificationDto;
import com.achiever.menschenfahren.base.model.NotificationStatus;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.controller.NotificationRestControllerInterface;
import com.achiever.menschenfahren.entities.notification.Notification;
import com.achiever.menschenfahren.exception.InvalidNotificationException;
import com.achiever.menschenfahren.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.mapper.NotificationMapper;
import com.achiever.menschenfahren.service.NotificationService;

/**
 *
 * @author Srijan Bajracharya
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
public class NotificationRestController extends BaseController implements NotificationRestControllerInterface {

    private final NotificationService notificationService;

    private final NotificationMapper  notificationMapper;

    @Autowired
    public NotificationRestController(@Nonnull final NotificationService notificationService) {
        this.notificationService = notificationService;
        this.notificationMapper = new NotificationMapper();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<NotificationDto>> createNotification(@Nonnull @Valid final NotificationCreateDto request, final boolean alsoVoided)
            throws InvalidNotificationException {

        if (StringUtils.isAnyBlank(request.getOriginalSenderId(), request.getOriginalReceiverId(), request.getEventId())) {
            throw new InvalidNotificationException("The id of sender, receiver and event should be filled.");
        }

        final Notification notification = this.notificationMapper.convertNotificationCreateDtoToNotification(request);

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

            if (notification.getOriginalSenderId().equals(originalSenderId) && notification.getOriginalReceiverId().equals(originalRecieverId)) {
                notification.setNotificationStatus(NotificationStatus.getByName(request.getNotificationStatus()));
                notification.setModifiedTimestamp(new Date());

                Notification savedNotification = this.notificationService.createNotification(notification);

                if (savedNotification != null) {
                    NotificationDto notificationDto = this.notificationMapper.convertNotificationToNotificationDto(savedNotification);
                    return buildResponse(notificationDto, HttpStatus.CREATED);
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
                allNotificationDto.add(this.notificationMapper.convertNotificationToNotificationDto(notification));
            }
            return buildResponse(allNotificationDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

}
