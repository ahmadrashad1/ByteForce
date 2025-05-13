package com.SportSync2.service;

import com.SportSync2.dto.GroundBookingRequestDTO;
import com.SportSync2.dto.GroundBookingResponseDTO;
import com.SportSync2.entity.Ground;
import com.SportSync2.entity.GroundBooking;
import com.SportSync2.entity.User;
import com.SportSync2.repository.GroundBookingRepository;
import com.SportSync2.repository.GroundRepository;
import com.SportSync2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroundBookingServiceTest {

    private GroundBookingRepository bookingRepo;
    private GroundRepository groundRepo;
    private UserRepository userRepo;
    private GroundBookingService service;

    @BeforeEach
    void setUp() {
        bookingRepo = mock(GroundBookingRepository.class);
        groundRepo = mock(GroundRepository.class);
        userRepo = mock(UserRepository.class);
        service = new GroundBookingService(bookingRepo, groundRepo, userRepo);
    }

    @Test
    void testBookGround_shouldThrowException_whenUserIdIsMissing() {
        GroundBookingRequestDTO dto = GroundBookingRequestDTO.builder()
                .groundId(1L)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.bookGround(dto));
        assertEquals("All fields in booking request must be provided", exception.getMessage());
    }

    @Test
    void testBookGround_shouldThrowException_whenGroundIsAlreadyBooked() {
        Long userId = 1L;
        Long groundId = 2L;

        GroundBookingRequestDTO dto = GroundBookingRequestDTO.builder()
                .groundId(groundId)
                .userId(userId)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .build();

        when(groundRepo.findById(groundId)).thenReturn(Optional.of(new Ground()));
        when(userRepo.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingRepo.findByGroundIdAndStartTimeLessThanAndEndTimeGreaterThan(
                eq(groundId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(new GroundBooking()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.bookGround(dto));
        assertEquals("Ground is already booked during this time", exception.getMessage());
    }

    @Test
    void testBookGround_shouldSucceed_whenInputIsValidAndGroundAvailable() {
        Long userId = 1L;
        Long groundId = 2L;

        Ground ground = Ground.builder()
                .id(groundId)
                .name("Main Ground")
                .location("City Center")
                .build();

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password")
                .verified(true)
                .build();

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        GroundBookingRequestDTO dto = GroundBookingRequestDTO.builder()
                .groundId(groundId)
                .userId(userId)
                .startTime(start)
                .endTime(end)
                .build();

        when(groundRepo.findById(groundId)).thenReturn(Optional.of(ground));
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepo.findByGroundIdAndStartTimeLessThanAndEndTimeGreaterThan(
                eq(groundId), eq(end), eq(start)))
                .thenReturn(Collections.emptyList());

        ArgumentCaptor<GroundBooking> captor = ArgumentCaptor.forClass(GroundBooking.class);
        when(bookingRepo.save(captor.capture())).thenAnswer(i -> {
            GroundBooking booking = captor.getValue();
            booking.setId(100L);
            return booking;
        });

        GroundBookingResponseDTO response = service.bookGround(dto);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Main Ground", response.getGround().getName());
        assertEquals("City Center", response.getGround().getLocation());
        assertEquals(start, response.getStartTime());
        assertEquals(end, response.getEndTime());
    }
}
