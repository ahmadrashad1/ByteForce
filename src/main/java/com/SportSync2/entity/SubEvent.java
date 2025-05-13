package com.SportSync2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean teamBased;
    private int maxParticipants;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    private Ground ground;

    @OneToOne
    private GroundBooking booking;
}

