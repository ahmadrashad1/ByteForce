package com.SportSync2.controller;

import com.SportSync2.dto.EventDTO;
import com.SportSync2.service.EventService;
import com.SportSync2.service.MyEventsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/my-events")
@RequiredArgsConstructor
public class MyEventsController {

    private final MyEventsService myEventsService;
    private final EventService eventService;

    @GetMapping("/my/{userId}")
    public ResponseEntity<List<EventDTO>> getMyEvents(@PathVariable Long userId) {
        List<EventDTO> myEvents = eventService.getMyEvents(userId);
        return ResponseEntity.ok(myEvents);
    }

    // Delete the event (only for the host)
    @DeleteMapping("/delete/{eventId}/{userId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        try {
            myEventsService.deleteEvent(eventId, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update the event details (time, participants, sub-events)
    @PutMapping("/update/{eventId}/{userId}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable @Valid Long eventId,
                                                @PathVariable @Valid Long userId,
                                                @RequestBody @Valid EventDTO eventDTO) {
        try {
            EventDTO updatedEvent = myEventsService.updateEvent(eventId, userId, eventDTO);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
