package com.SportSync2.service;

import com.SportSync2.dto.ParticipantDTO;
import com.SportSync2.dto.ResponseDTO;
import com.SportSync2.dto.TeamMemberDTO;
import com.SportSync2.entity.*;
import com.SportSync2.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final UserRepository userRepository;
    private final SubEventRepository subEventRepository;
    private final ParticipantRepository participantRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    public ResponseDTO registerParticipant(ParticipantDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("participant DTOs cannot be null");
        }
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        Optional<SubEvent> subEventOpt = subEventRepository.findById(dto.getSubEventId());

        if (userOpt.isEmpty() || subEventOpt.isEmpty()) {
            return new ResponseDTO(false, "Invalid user or sub-event");
        }

        User user = userOpt.get();
        SubEvent subEvent = subEventOpt.get();

        if (subEvent.getEvent().getHost().getId().equals(user.getId())) {
            return new ResponseDTO(false, "Hosts cannot register for their own event.");
        }

        if (participantRepository.existsByUserAndSubEvent(user, subEvent)) {
            return new ResponseDTO(false, "User is already registered for this event");
        }

        // Handle team-based registration
        if (subEvent.isTeamBased()) {
            if (dto.getTeamName() == null || dto.getTeamMembers() == null || dto.getTeamMembers().isEmpty()) {
                return new ResponseDTO(false, "Team name and members are required for team-based events");
            }

            Team team = Team.builder()
                    .name(dto.getTeamName())
                    .createdBy(user)
                    .subEvent(subEvent)
                    .build();
            team = teamRepository.save(team);

            // Save team members
            for (TeamMemberDTO memberDTO : dto.getTeamMembers()) {
                if (memberDTO.getEmail() == null || memberDTO.getName() == null) {
                    return new ResponseDTO(false, "Each team member must have a name and email.");
                }

                TeamMember member = TeamMember.builder()
                        .name(memberDTO.getName())
                        .email(memberDTO.getEmail())
                        .team(team)
                        .build();
                teamMemberRepository.save(member);
            }

            // Register creator as participant
            Participant participant = Participant.builder()
                    .user(user)
                    .subEvent(subEvent)
                    .team(team)
                    .build();
            participantRepository.save(participant);

            return new ResponseDTO(true, "Team registered successfully!");
        }

        // Individual registration
        Participant participant = Participant.builder()
                .user(user)
                .subEvent(subEvent)
                .build();
        participantRepository.save(participant);

        return new ResponseDTO(true, "Successfully registered as an individual participant!");
    }
}
