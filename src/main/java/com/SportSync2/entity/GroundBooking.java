
package com.SportSync2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroundBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Ground ground;

    @ManyToOne
    private User user;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean isEventBooking; // true if booked for an event

}
