package com.SportSync2.controller;

import com.SportSync2.dto.SubEventDTO;
import com.SportSync2.service.MySubEventService;
import com.SportSync2.service.SubEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sub-events")
@RequiredArgsConstructor
public class MySubEventController {

    private final MySubEventService mySubEventService;

    @PutMapping("/update/{eventId}/{userId}")
    public ResponseEntity<String> updateSubEvents(@PathVariable Long eventId,
                                                  @PathVariable Long userId,
                                                  @RequestBody @Valid List<SubEventDTO> subEventDTOs) {
        try {
            mySubEventService.updateSubEvents(eventId, subEventDTOs, userId);
            return ResponseEntity.ok("Sub-events updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
