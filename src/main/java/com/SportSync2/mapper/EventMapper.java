package com.SportSync2.mapper;

import com.SportSync2.dto.EventDTO;
import com.SportSync2.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public Event toEntity(EventDTO dto) {
        return Event.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();   ///  host id is set in service
    }

    public EventDTO toDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .createdByUserId(event.getCreatedBy() != null ? event.getCreatedBy().getId() : null)
                //.hostID(event.getHost() != null ? event.getHost().getId() : null)
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                //.groundId(event.getGround() != null ? event.getGround().getId() : null)
                .build();
    }
}
