package com.SportSync2.mapper;

import com.SportSync2.dto.ParticipantDTO;
import com.SportSync2.entity.Participant;
import com.SportSync2.entity.SubEvent;
import com.SportSync2.entity.Team;
import com.SportSync2.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ParticipantMapper {

    public Participant toEntity(ParticipantDTO dto, User user, SubEvent subEvent, Team team) {
        return Participant.builder()
                .user(user)
                .subEvent(subEvent)
                .team(team) // can be null for individual-based
                .build();
    }

}
