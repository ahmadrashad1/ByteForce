
package com.SportSync2.controller;

import com.SportSync2.dto.*;
import com.SportSync2.service.GroundBookingService;
import com.SportSync2.service.GroundService;
import com.SportSync2.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grounds")
@RequiredArgsConstructor
public class GroundController {

    private final GroundService groundService;

    @GetMapping("/available")
    public ResponseEntity<List<GroundDTO>> getAllGrounds() {
        return ResponseEntity.ok(groundService.getAllGrounds());
    }
}
