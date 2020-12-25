package com.achiever.menschenfahren.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.achiever.menschenfahren.entities.notification.Notification;

/**
 * Interface handling all functionality for notification.
 *
 * @author Srijan Bajracharya
 *
 */
public interface NotificationService {

    /**
     * creates notification from the given request.
     *
     * @param notification
     *            The object to be saved.
     * @return The saved notification object.
     */
    Notification createNotification(@Nonnull final Notification notification);

    /**
     * Find notification by id.
     *
     * @param notificationId
     *            The id of notification.
     * @return Returns the notification.
     */
    Optional<Notification> findById(@Nonnull final String notificationId);

    /**
     * Finds notification by original sender id and voided.
     *
     * @param originalSenderId
     *            The id of user who first send the notification.
     * @param alsoVoided
     *            The flag if the notifiation is cancelled.
     * @return The list of notification.
     */
    List<Notification> findByOriginalSenderId(@Nonnull final String originalSenderId, final boolean alsoVoided);

    /**
     * Finds notification by original receiver id and voided.
     *
     * @param originalReceiverId
     *            The id of user who first received the notification.
     * @param alsoVoided
     *            The flag if the notification is cancelled
     * @return The list of notification.
     */
    List<Notification> findByOriginalReceiverId(@Nonnull final String originalReceiverId, final boolean alsoVoided);

}
