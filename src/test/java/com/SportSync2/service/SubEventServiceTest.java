package com.SportSync2.service;

import com.SportSync2.dto.SubEventDTO;
import com.SportSync2.entity.Event;
import com.SportSync2.entity.Ground;
import com.SportSync2.entity.GroundBooking;
import com.SportSync2.entity.SubEvent;
import com.SportSync2.mapper.SubEventMapper;
import com.SportSync2.repository.EventRepository;
import com.SportSync2.repository.GroundBookingRepository;
import com.SportSync2.repository.GroundRepository;
import com.SportSync2.repository.SubEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Ensure Mockito initializes the mocks
class SubEventServiceImplTest {

    @Mock
    private SubEventRepository subEventRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private GroundRepository groundRepository;

    @Mock
    private GroundBookingRepository groundBookingRepository;

    @Mock
    private SubEventMapper subEventMapper;

    @InjectMocks
    private SubEventServiceImpl subEventService;

    private Event event;
    private Ground ground;
    private SubEventDTO subEventDTO;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);

        ground = new Ground();
        ground.setId(1L);

        subEventDTO = new SubEventDTO();
        subEventDTO.setGroundId(ground.getId());
        subEventDTO.setStartTime(LocalDateTime.now().plusHours(1));
        subEventDTO.setEndTime(LocalDateTime.now().plusHours(2));
        subEventDTO.setName("Football Match");
    }

    @Test
    void testCreateSubEvents_shouldThrowExceptionIfGroundIsAlreadyBooked() {
        List<SubEventDTO> subEventDTOs = Arrays.asList(subEventDTO);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(groundRepository.findById(ground.getId())).thenReturn(Optional.of(ground));
        when(groundBookingRepository.findByGroundIdAndStartTimeLessThanAndEndTimeGreaterThan(anyLong(), any(), any()))
                .thenReturn(Arrays.asList(new GroundBooking()));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                subEventService.createSubEvents(1L, subEventDTOs));
        assertEquals("Ground is already booked for sub-event Football Match", exception.getMessage());
    }

    @Test
    void testCreateSubEvents_shouldThrowExceptionIfGroundNotFound() {
        List<SubEventDTO> subEventDTOs = Arrays.asList(subEventDTO);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(groundRepository.findById(ground.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                subEventService.createSubEvents(1L, subEventDTOs));
        assertEquals("Ground not found with ID: 1", exception.getMessage());
    }

    @Test
    void testCreateSubEvents_shouldCreateSubEventSuccessfully() {
        List<SubEventDTO> subEventDTOs = Arrays.asList(subEventDTO);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(groundRepository.findById(ground.getId())).thenReturn(Optional.of(ground));
        when(groundBookingRepository.findByGroundIdAndStartTimeLessThanAndEndTimeGreaterThan(anyLong(), any(), any()))
                .thenReturn(Arrays.asList());
        when(subEventMapper.toEntity(subEventDTO)).thenReturn(new SubEvent());


        subEventService.createSubEvents(1L, subEventDTOs);

        verify(groundBookingRepository, times(1)).save(any(GroundBooking.class));
        verify(subEventRepository, times(1)).save(any(SubEvent.class));
    }
}
