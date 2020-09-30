package com.achiever.menschenfahren.entities.events;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * CreatedBy : edangol
 * CreatedOn : 10/04/2020
 * Description :
 **/
@Data
@ToString
@Entity
@Table(name = "event_gallery")
public class EventGallery implements Serializable {

    @Id
    private long id;

    @ManyToOne(targetEntity = Event.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @Column(name = "photo")
    private String photo;

    @Column(name = "caption")
    private String caption;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @Column(name = "uploaded_on")
    private long uploadedOn;

}
