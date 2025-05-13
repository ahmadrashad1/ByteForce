
package com.SportSync2.controller;

import com.SportSync2.dto.*;
import com.SportSync2.service.GroundBookingService;
import com.SportSync2.service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class GroundBookingController {

    private final GroundBookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<GroundBookingResponseDTO> bookGround(@RequestBody @Valid GroundBookingRequestDTO dto) {
        return ResponseEntity.ok(bookingService.bookGround(dto));
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroundBookingResponseDTO>> getUserBookings(@PathVariable @Valid Long userId) {
        List<GroundBookingResponseDTO> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }
}

