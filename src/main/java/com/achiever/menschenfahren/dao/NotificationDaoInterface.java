package com.achiever.menschenfahren.dao;

import java.util.List;

import javax.annotation.Nonnull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.notification.Notification;

/**
 * Dao handling all functionality for Notification.
 *
 * @author Srijan Bajracharya
 *
 */
@Repository
public interface NotificationDaoInterface extends JpaRepository<Notification, String> {

    /**
     * Finds all notification by original sender id.
     *
     * @param originalSenderId
     *            The id of original sender of notification.
     * @return The list of notification.
     */
    List<Notification> findByOriginalSenderId(@Nonnull final String originalSenderId);

    /**
     * Finds all notification by original receiver id.
     *
     * @param originalReceiverId
     *            The id of original receiver of notification.
     * @return The list of notification.
     */
    List<Notification> findByOriginalReceiverId(@Nonnull final String originalReceiverId);

    /**
     * Finds notification based on receiver id and voided.
     * 
     * @param originalReceiverId
     *            The id of original receiver.
     * @param voided
     *            The flag if the notification is cancelled.
     * @return The list of notification.
     */
    List<Notification> findByOriginalReceiverIdAndVoided(@Nonnull final String originalReceiverId, final boolean voided);

    /**
     * Finds notification based on sender id and voided.
     * 
     * @param originalSenderId
     *            The id of original sender.
     * @param voided
     *            The flag if the notification is cancelled.
     * @return The list of notification.
     */
    List<Notification> findByOriginalSenderIdAndVoided(@Nonnull final String originalSenderId, final boolean voided);

}
