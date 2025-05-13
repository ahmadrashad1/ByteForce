//package com.SportSync2.mapper;
//
//import com.SportSync2.dto.TeamDTO;
//import com.SportSync2.entity.SubEvent;
//import com.SportSync2.entity.Team;
//import com.SportSync2.entity.User;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class TeamMapper {
//
//    public Team toEntity(TeamDTO dto, SubEvent subEvent, User createdBy, List<User> members) {
//        return Team.builder()
//               // .teamName(dto.getTeamName())
//                .subEvent(subEvent)
//                .createdBy(createdBy)
//                .members(members)
//                .build();
//    }
//}
