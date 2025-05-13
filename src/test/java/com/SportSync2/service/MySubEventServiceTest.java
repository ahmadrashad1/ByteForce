package com.SportSync2.service;

import com.SportSync2.dto.SubEventDTO;
import com.SportSync2.entity.Event;
import com.SportSync2.entity.SubEvent;
import com.SportSync2.entity.User;
import com.SportSync2.repository.EventRepository;
import com.SportSync2.repository.SubEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MySubEventServiceTest {

    @Mock
    private SubEventRepository subEventRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private MySubEventService mySubEventService;

    private Event event;
    private User host;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Host user
        host = User.builder()
                .id(1L)
                .email("host@example.com")
                .password("password")
                .verified(true)
                .build();

        // Event with matching ID
        event = Event.builder()
                .id(100L)
                .host(host)
                .name("Football Tournament")
                .description("Exciting match")
                .location("Stadium A")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .build();
    }

    @Test
    void testUpdateSubEvents_Success() {
        Long userId = 1L;

        SubEventDTO dto1 = SubEventDTO.builder()
                .name("Futsal")
                .teamBased(true)
                .maxParticipants(10)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .groundId(1L)
                .build();

        SubEventDTO dto2 = SubEventDTO.builder()
                .name("Basketball")
                .teamBased(true)
                .maxParticipants(12)
                .startTime(LocalDateTime.now().plusDays(2))
                .endTime(LocalDateTime.now().plusDays(2).plusHours(1))
                .groundId(2L)
                .build();

        List<SubEventDTO> subEventDTOs = List.of(dto1, dto2);

        when(eventRepository.findById(100L)).thenReturn(Optional.of(event));
        when(subEventRepository.findByEventId(100L)).thenReturn(List.of());
        when(subEventRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        mySubEventService.updateSubEvents(100L, subEventDTOs, userId);

        ArgumentCaptor<List<SubEvent>> captor = ArgumentCaptor.forClass(List.class);
        verify(subEventRepository).saveAll(captor.capture());

        List<SubEvent> saved = captor.getValue();
        assertEquals(2, saved.size());
        assertEquals("Futsal", saved.get(0).getName());
        assertEquals("Basketball", saved.get(1).getName());
    }

    @Test
    void testUpdateSubEvents_EventNotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                mySubEventService.updateSubEvents(999L, List.of(), 1L)
        );

        assertEquals("Event not found", exception.getMessage());
    }

    @Test
    void testUpdateSubEvents_UnauthorizedUser() {
        Long unauthorizedUserId = 2L;
        when(eventRepository.findById(100L)).thenReturn(Optional.of(event));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                mySubEventService.updateSubEvents(100L, List.of(), unauthorizedUserId)
        );

        assertEquals("You are not authorized to update this event's sub-events.", exception.getMessage());
    }
}
