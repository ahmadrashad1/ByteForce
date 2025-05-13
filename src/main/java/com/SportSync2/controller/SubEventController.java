package com.SportSync2.controller;

import com.SportSync2.dto.SubEventDTO;
import com.SportSync2.entity.SubEvent;
import com.SportSync2.mapper.EventMapper;
import com.SportSync2.mapper.SubEventMapper;
import com.SportSync2.service.SubEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sub-events")
@RequiredArgsConstructor
public class SubEventController {

    private final SubEventService subEventService;
    private final SubEventMapper subEventMapper;

    @PostMapping("/create/{eventId}")
    public ResponseEntity<String> createSubEvents(@PathVariable @Valid Long eventId, @RequestBody @Valid List<SubEventDTO> subEvents) {
        subEventService.createSubEvents(eventId, subEvents);
        return ResponseEntity.ok("Sub-events successfully created for event with id " + eventId + ".");
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<SubEventDTO>> getSubEventsByEvent(@PathVariable Long eventId)
    {
        List<SubEventDTO> subEvents = subEventService.getSubEventsByEventId(eventId);
        return ResponseEntity.ok(subEvents);
    }


//    @PostMapping("/{eventID}/add-sub-event")
//    public ResponseEntity<SubEventDTO> addSubEvent(@PathVariable Long eventId, @RequestBody SubEventDTO subEventDTO) {
//        SubEvent subEvent = subEventService.addSubEvent(eventId, subEventDTO);
//        return ResponseEntity.ok(subEventMapper.toDto(subEvent));
//    }


}
