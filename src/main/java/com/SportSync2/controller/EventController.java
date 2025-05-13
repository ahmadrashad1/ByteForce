package com.SportSync2.controller;

import com.SportSync2.dto.EventDTO;
import com.SportSync2.mapper.EventMapper;
import com.SportSync2.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;


    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventDTO eventDTO) {


        try {
            var event = eventService.createEvent(eventDTO, eventDTO.getCreatedByUserId());
            return ResponseEntity.ok(eventMapper.toDTO(event));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Failed to create event: " + ex.getMessage());
        }
    }

    @GetMapping("/allexceptmine/{userId}")
    public ResponseEntity<List<EventDTO>> getAllEventsExceptMine(@PathVariable @Valid Long userId) {
        List<EventDTO> events = eventService.getAllEventsExceptMine(userId);
        return ResponseEntity.ok(events);
    }
}
