package com.SportSync2.service;

import com.SportSync2.dto.EventDTO;
import com.SportSync2.entity.Event;
import com.SportSync2.entity.SubEvent;
import com.SportSync2.entity.User;
import com.SportSync2.repository.EventRepository;
import com.SportSync2.repository.SubEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyEventsServiceTest {

    private EventRepository eventRepository;
    private SubEventRepository subEventRepository;
    private MyEventsService myEventsService;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        subEventRepository = mock(SubEventRepository.class);
        myEventsService = new MyEventsService(eventRepository, subEventRepository);
    }

    @Test
    void deleteEvent_shouldDeleteEventAndSubEvents_whenUserIsHost() {
        Long eventId = 1L;
        Long userId = 100L;

        User host = User.builder().id(userId).build();
        Event event = Event.builder().id(eventId).host(host).build();
        List<SubEvent> subEvents = List.of(
                SubEvent.builder().id(1L).event(event).build(),
                SubEvent.builder().id(2L).event(event).build()
        );

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(subEventRepository.findByEventId(eventId)).thenReturn(subEvents);

        myEventsService.deleteEvent(eventId, userId);

        verify(subEventRepository).deleteAll(subEvents);
        verify(eventRepository).delete(event);
    }

    @Test
    void deleteEvent_shouldThrowException_whenUserIsNotHost() {
        Long eventId = 1L;
        Long userId = 100L;
        Long differentHostId = 200L;

        Event event = Event.builder().id(eventId).host(User.builder().id(differentHostId).build()).build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            myEventsService.deleteEvent(eventId, userId);
        });

        assertTrue(exception.getMessage().contains("not authorized"));
    }

    @Test
    void updateEvent_shouldUpdateFields_whenUserIsHost() {
        Long eventId = 1L;
        Long userId = 100L;

        User host = User.builder().id(userId).build();
        Event existingEvent = Event.builder()
                .id(eventId)
                .name("Old Name")
                .description("Old Desc")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .host(host)
                .createdBy(host)
                .build();

        EventDTO eventDTO = EventDTO.builder()
                .id(eventId)
                .name("New Name")
                .description("New Desc")
                .createdByUserId(userId)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArgument(0));
        when(subEventRepository.findByEventId(eventId)).thenReturn(List.of());

        EventDTO updated = myEventsService.updateEvent(eventId, userId, eventDTO);

        assertEquals("New Name", updated.getName());
        assertEquals("New Desc", updated.getDescription());
        assertEquals(eventDTO.getStartTime(), updated.getStartTime());

        verify(eventRepository).save(existingEvent);
    }

    @Test
    void updateEvent_shouldThrowException_whenUserIsNotHost() {
        Long eventId = 1L;
        Long userId = 100L;
        Long differentHostId = 200L;

        Event event = Event.builder().id(eventId).host(User.builder().id(differentHostId).build()).build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        EventDTO eventDTO = EventDTO.builder()
                .id(eventId)
                .name("New Name")
                .description("New Desc")
                .createdByUserId(userId)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .build();

        Exception exception = assertThrows(RuntimeException.class, () ->
                myEventsService.updateEvent(eventId, userId, eventDTO)
        );

        assertTrue(exception.getMessage().contains("not authorized"));
    }
}
