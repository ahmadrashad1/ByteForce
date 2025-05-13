package com.SportSync2.mapper;

import com.SportSync2.dto.SubEventDTO;
import com.SportSync2.entity.SubEvent;
import org.springframework.stereotype.Component;

@Component
public class SubEventMapper {

    public SubEvent toEntity(SubEventDTO dto) {
        return SubEvent.builder()
                .name(dto.getName())
                .teamBased(dto.isTeamBased())
                .maxParticipants(dto.getMaxParticipants())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();
    }

    public SubEventDTO toDto(SubEvent subEvent) {
        return SubEventDTO.builder()
                .name(subEvent.getName())
                .teamBased(subEvent.isTeamBased())
                .maxParticipants(subEvent.getMaxParticipants())
                .startTime(subEvent.getStartTime())
                .endTime(subEvent.getEndTime())
                .groundId(subEvent.getGround() != null ? subEvent.getGround().getId() : null)
                .build();
    }

}
