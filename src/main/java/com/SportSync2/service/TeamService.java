//package com.SportSync2.service;
//
//import com.SportSync2.dto.ResponseDTO;
//import com.SportSync2.dto.TeamDTO;
//import com.SportSync2.entity.SubEvent;
//import com.SportSync2.entity.Team;
//import com.SportSync2.entity.User;
//import com.SportSync2.repository.SubEventRepository;
//import com.SportSync2.repository.TeamRepository;
//import com.SportSync2.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class TeamService {
//
//    private final TeamRepository teamRepository;
//    private final UserRepository userRepository;
//    private final SubEventRepository subEventRepository;
//
//    public ResponseDTO createTeam(TeamDTO teamDTO) {
//        Optional<User> creatorOpt = userRepository.findById(teamDTO.getCreatedByUserId());
//        Optional<SubEvent> subEventOpt = subEventRepository.findById(teamDTO.getSubEventId());
//
//        if (creatorOpt.isEmpty() || subEventOpt.isEmpty()) {
//            return new ResponseDTO(false, "Invalid user or sub-event ID");
//        }
//
//        SubEvent subEvent = subEventOpt.get();
//        if (!subEvent.isTeamBased()) {
//            return new ResponseDTO(false, "Cannot create a team for an individual event");
//        }
//
//        Team team = Team.builder()
//                .name(teamDTO.getName())
//                .createdBy(creatorOpt.get())
//                .subEvent(subEvent)
//                .build();
//
//        teamRepository.save(team);
//
//        return new ResponseDTO(true, "Team created successfully!");
//    }
//}
