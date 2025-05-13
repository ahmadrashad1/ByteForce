//package com.SportSync2.controller;
//
//import com.SportSync2.dto.ResponseDTO;
//import com.SportSync2.dto.TeamDTO;
//import com.SportSync2.service.TeamService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/teams")
//@RequiredArgsConstructor
//public class TeamController {
//
//    private final TeamService teamService;
//
//    @PostMapping("/create")
//    public ResponseEntity<ResponseDTO> createTeam(@RequestBody TeamDTO teamDTO) {
//        ResponseDTO response = teamService.createTeam(teamDTO);
//        return response.isSuccess()
//                ? ResponseEntity.ok(response)
//                : ResponseEntity.badRequest().body(response);
//    }
//}
