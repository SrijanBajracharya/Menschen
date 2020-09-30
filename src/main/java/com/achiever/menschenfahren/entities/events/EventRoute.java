package com.achiever.menschenfahren.entities.events;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

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
@Table(name = "event_routes")
public class EventRoute {

    @Id
    private long id;

    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @Column(name = "precedence_index")
    private int precedenceIndex;

    @Column(name = "starting_point")
    private String startingPoint;

    @Column(name = "ending_point")
    private String endingPoint;

}
