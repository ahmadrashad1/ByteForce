package com.SportSync2.service;

import com.SportSync2.dto.*;
import com.SportSync2.entity.Ground;
import com.SportSync2.entity.GroundBooking;
import com.SportSync2.entity.User;
import com.SportSync2.repository.GroundBookingRepository;
import com.SportSync2.repository.GroundRepository;
import com.SportSync2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroundBookingService {

    private final GroundBookingRepository bookingRepo;
    private final GroundRepository groundRepo;
    private final UserRepository userRepo;

    public GroundBookingResponseDTO bookGround(GroundBookingRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Booking request cannot be null");
        }
        if (dto.getGroundId() == null || dto.getUserId() == null || dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new IllegalArgumentException("All fields in booking request must be provided");
        }

        Ground ground = groundRepo.findById(dto.getGroundId())
                .orElseThrow(() -> new RuntimeException("Ground not found"));

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isBooked = !bookingRepo.findByGroundIdAndStartTimeLessThanAndEndTimeGreaterThan(
                dto.getGroundId(), dto.getEndTime(), dto.getStartTime()).isEmpty();

        if (isBooked) {
            throw new RuntimeException("Ground is already booked during this time");
        }

        GroundBooking booking = GroundBooking.builder()
                .ground(ground)
                .user(user)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();

        bookingRepo.save(booking);

        return GroundBookingResponseDTO.builder()
                .id(booking.getId())
                .ground(GroundDTO.builder()
                        .id(ground.getId())
                        .name(ground.getName())
                        .location(ground.getLocation())
                        .build())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .build();
    }

    public String cancelBooking(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID must not be null");
        }

        GroundBooking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot cancel past bookings");
        }

        bookingRepo.delete(booking);
        return "Booking cancelled successfully";
    }

    public List<GroundBookingResponseDTO> getUserBookings(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        List<GroundBooking> bookings = bookingRepo.findByUserId(userId);

        return bookings.stream()
                .map(booking -> GroundBookingResponseDTO.builder()
                        .id(booking.getId())
                        .ground(GroundDTO.builder()
                                .id(booking.getGround().getId())
                                .name(booking.getGround().getName())
                                .location(booking.getGround().getLocation())
                                .build())
                        .startTime(booking.getStartTime())
                        .endTime(booking.getEndTime())
                        .build())
                .collect(Collectors.toList());
    }
}
