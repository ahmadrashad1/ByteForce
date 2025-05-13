package com.SportSync2.controller;

import com.SportSync2.dto.ResponseDTO;
import com.SportSync2.dto.ParticipantDTO;
import com.SportSync2.service.ParticipantService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerParticipant(@RequestBody ParticipantDTO participantDTO) {
        ResponseDTO response = participantService.registerParticipant(participantDTO);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }
}

