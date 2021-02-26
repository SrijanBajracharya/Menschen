package com.achiever.menschenfahren.entities.notification;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.achiever.menschenfahren.base.model.NotificationStatus;
import com.achiever.menschenfahren.base.model.NotificationType;
import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.model.AbstractBaseEntity;
import com.achiever.menschenfahren.entities.users.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@Entity(name = "notification")
@Table
@EqualsAndHashCode(callSuper = true)
public class Notification extends AbstractBaseEntity {

    @Id
    @Nonnull
    private String             id;

    /** Id of an user who sends a request to join or who invites to join the event. **/
    @Nonnull
    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "original_sender_id", referencedColumnName = "id")
    private User               originalSender;

    /** Id of an user who receives the invite or who receives the request for the event. **/
    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "original_receiver_id", referencedColumnName = "id")
    @Nonnull
    private User               originalReceiver;

    /** Type of notification whether it is a request to join the event or invite to join the event. **/
    @Nonnull
    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType   notificationType;

    /** Status defining state of the notification **/
    @Nonnull
    @Column(name = "notification_status")
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    /** If the request is cancelled. **/
    @Column(name = "voided")
    private boolean            voided;

    /** The id of an event. **/
    @Nonnull
    @OneToOne(targetEntity = Event.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event              event;

    /** The modified timestamp of a notification. **/
    @Column(name = "modified_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date               modifiedTimestamp;

    public Notification() {
        super();
        this.id = UUID.randomUUID().toString();
        this.modifiedTimestamp = createdTimestamp;
    }

}
