package com.SportSync2.service;

import com.SportSync2.dto.EventDTO;
import com.SportSync2.dto.SubEventDTO;
import com.SportSync2.entity.Event;
import com.SportSync2.entity.SubEvent;
import com.SportSync2.repository.EventRepository;
import com.SportSync2.repository.SubEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyEventsService {

    private final EventRepository eventRepository;
    private final SubEventRepository subEventRepository;

    @Transactional
    public void deleteEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        if (!event.getHost().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this event.");
        }

        List<SubEvent> subEvents = subEventRepository.findByEventId(eventId);
        subEventRepository.deleteAll(subEvents);

        eventRepository.delete(event);
    }

    @Transactional
    public EventDTO updateEvent(Long eventId, Long userId, EventDTO eventDTO) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

            if (!event.getHost().getId().equals(userId)) {
                throw new RuntimeException("You are not authorized to update this event.");
            }

            event.setName(eventDTO.getName());
            event.setDescription(eventDTO.getDescription());
            event.setStartTime(eventDTO.getStartTime());
            event.setEndTime(eventDTO.getEndTime());

            Event updatedEvent = eventRepository.save(event);

            List<SubEvent> subEvents = subEventRepository.findByEventId(eventId);

            List<SubEventDTO> subEventDTOs = subEvents.stream()
                    .map(subEvent -> SubEventDTO.builder()
                            .name(subEvent.getName())
                            .teamBased(subEvent.isTeamBased())
                            .maxParticipants(subEvent.getMaxParticipants())
                            .build())
                    .toList();

            return EventDTO.builder()
                    .id(updatedEvent.getId())
                    .name(updatedEvent.getName())
                    .description(updatedEvent.getDescription())
                    .createdByUserId(updatedEvent.getCreatedBy() != null ? updatedEvent.getCreatedBy().getId() : null)
                    .startTime(updatedEvent.getStartTime())
                    .endTime(updatedEvent.getEndTime())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error updating event: " + e.getMessage(), e);
        }
    }
}
