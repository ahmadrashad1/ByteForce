//package com.SportSync2.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Booking {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    private User user;
//
//    @ManyToOne
//    private Ground ground;
//
//    private LocalDate date;
//
//    private LocalTime startTime;
//    private LocalTime endTime;
//
//    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
//    private Event event; // if this booking is linked to an event
//}
