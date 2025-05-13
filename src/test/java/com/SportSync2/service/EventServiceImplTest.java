package com.SportSync2.service;

import com.SportSync2.dto.EventDTO;
import com.SportSync2.entity.Event;
import com.SportSync2.entity.User;
import com.SportSync2.mapper.EventMapper;
import com.SportSync2.repository.EventRepository;
import com.SportSync2.repository.GroundBookingRepository;
import com.SportSync2.repository.GroundRepository;
import com.SportSync2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private GroundRepository groundRepository;

    @Mock
    private GroundBookingRepository groundBookingRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private EventDTO sampleDto;
    private Event sampleEvent;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleDto = EventDTO.builder()
                .name("Football Tournament")
                .description("A great football event for all skill levels")
                .createdByUserId(1L)
                .startTime(LocalDateTime.of(2025, 5, 10, 14, 0))
                .endTime(LocalDateTime.of(2025, 5, 10, 16, 0))
                .build();

        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setEmail("host@example.com");

        sampleEvent = new Event();
        sampleEvent.setId(1L);
        sampleEvent.setName("Football Tournament");
    }

    // ----------------------------
    // Tests for createEvent()
    // ----------------------------

    @Test
    void testCreateEvent_Correct() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(eventMapper.toEntity(sampleDto)).thenReturn(sampleEvent);
        when(eventRepository.save(any(Event.class))).thenReturn(sampleEvent);

        Event result = eventService.createEvent(sampleDto, 1L);
        assertNotNull(result);
        assertEquals("Football Tournament", result.getName());
        verify(eventRepository).save(sampleEvent);
    }

    @Test
    void testCreateEvent_HostNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventService.createEvent(sampleDto, 1L));

        assertEquals("Host not found with ID: 1", exception.getMessage());
    }

    @Test
    void testCreateEvent_NullDTO_ThrowsException() {
        assertThrows(NullPointerException.class,
                () -> eventService.createEvent(null, 1L));
    }

    // ----------------------------
    // Tests for getAllEventsExceptMine()
    // ----------------------------

    @Test
    void testGetAllEventsExceptMine_Correct() {
        Event event = new Event();
        event.setName("Public Match");

        EventDTO dto = EventDTO.builder()
                .name("Public Match")
                .description("An open football match")
                .createdByUserId(2L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .build();

        when(eventRepository.findByHost_IdNot(1L)).thenReturn(List.of(event));
        when(eventMapper.toDTO(event)).thenReturn(dto);

        List<EventDTO> result = eventService.getAllEventsExceptMine(1L);
        assertEquals(1, result.size());
        assertEquals("Public Match", result.get(0).getName());
    }

    @Test
    void testGetAllEventsExceptMine_EmptyList() {
        when(eventRepository.findByHost_IdNot(2L)).thenReturn(Collections.emptyList());

        List<EventDTO> result = eventService.getAllEventsExceptMine(2L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllEventsExceptMine_NullUserId_ThrowsException() {
        assertThrows(NullPointerException.class, () -> eventService.getAllEventsExceptMine(null));
    }

    // ----------------------------
    // Tests for getMyEvents()
    // ----------------------------

    @Test
    void testGetMyEvents_Correct() {
        Event event = new Event();
        event.setName("My Private Event");

        EventDTO dto = EventDTO.builder()
                .name("My Private Event")
                .description("My special private event")
                .createdByUserId(1L)
                .startTime(LocalDateTime.now().plusDays(2))
                .endTime(LocalDateTime.now().plusDays(2).plusHours(1))
                .build();

        when(eventRepository.findByHost_Id(1L)).thenReturn(List.of(event));
        when(eventMapper.toDTO(event)).thenReturn(dto);

        List<EventDTO> result = eventService.getMyEvents(1L);
        assertEquals(1, result.size());
        assertEquals("My Private Event", result.get(0).getName());
    }

    @Test
    void testGetMyEvents_NoEvents() {
        when(eventRepository.findByHost_Id(1L)).thenReturn(Collections.emptyList());

        List<EventDTO> result = eventService.getMyEvents(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMyEvents_NullUserId_ThrowsException() {
        assertThrows(NullPointerException.class, () -> eventService.getMyEvents(null));
    }
}
