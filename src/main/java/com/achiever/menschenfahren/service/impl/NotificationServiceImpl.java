package com.achiever.menschenfahren.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.achiever.menschenfahren.dao.NotificationDaoInterface;
import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.entities.notification.Notification;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

/**
 * Class implementing the interface handling all notification services.
 *
 * @author Srijan Bajracharya
 *
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDaoInterface notificationDao;

    private final UserDaoInterface         userDao;

    @Autowired
    public NotificationServiceImpl(final NotificationDaoInterface notificationDao, @Nonnull final UserDaoInterface userDao) {
        this.notificationDao = notificationDao;
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Notification createNotification(@Nonnull final Notification notification, @Nonnull final User senderUser, @Nonnull final User receiverUser) {
        this.userDao.save(senderUser);
        this.userDao.save(receiverUser);
        return notificationDao.save(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Notification> findById(String notificationId) {

        return notificationDao.findById(notificationId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Notification> findByOriginalSenderId(@Nonnull final String originalSenderId, final boolean alsoVoided) {

        if (alsoVoided) {
            return notificationDao.findByOriginalSenderId(originalSenderId);
        } else {
            return notificationDao.findByOriginalSenderIdAndVoided(originalSenderId, alsoVoided);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Notification> findByOriginalReceiverId(@Nonnull final String originalReceiverId, final boolean alsoVoided) {
        if (alsoVoided) {
            return notificationDao.findByOriginalReceiverId(originalReceiverId);
        } else {
            return notificationDao.findByOriginalReceiverIdAndVoided(originalReceiverId, alsoVoided);
        }
    }

    @Override
    public Notification findByOriginalSenderIdAndOriginalReceiverIdAndEventId(@Nonnull final String originalSenderId, @Nonnull final String originalReceiverId,
            @Nonnull final String eventId) {
        return notificationDao.findByOriginalSenderIdAndOriginalReceiverIdAndEventId(originalSenderId, originalReceiverId, eventId);
    }

    @Override
    public Notification updateNotification(@Nonnull final Notification notification) {
        return notificationDao.save(notification);
    }

}
