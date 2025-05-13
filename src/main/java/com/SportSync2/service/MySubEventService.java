package com.SportSync2.service;

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
public class MySubEventService {

    private final SubEventRepository subEventRepository;
    private final EventRepository eventRepository;

    @Transactional
    public void updateSubEvents(Long eventId, List<SubEventDTO> subEventDTOs, Long userId) {

        if (eventId == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }
        if (subEventDTOs == null) {
            throw new IllegalArgumentException("SubEvent DTOs cannot be null");
        }


        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getHost().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to update this event's sub-events.");
        }

        List<SubEvent> oldSubEvents = subEventRepository.findByEventId(eventId);
        subEventRepository.deleteAll(oldSubEvents);

        List<SubEvent> newSubEvents = subEventDTOs.stream().map(dto -> SubEvent.builder()
                .name(dto.getName())
                .teamBased(dto.isTeamBased())
                .maxParticipants(dto.getMaxParticipants())
                .event(event)
                .build()).toList();

        subEventRepository.saveAll(newSubEvents);
    }
}
