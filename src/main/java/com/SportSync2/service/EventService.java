package com.SportSync2.service;

import com.SportSync2.dto.EventDTO;
import com.SportSync2.dto.SubEventDTO;
import com.SportSync2.entity.Event;
import com.SportSync2.entity.SubEvent;

import java.util.List;


public interface EventService {
    Event createEvent(EventDTO eventDTO, Long hostId);


    // Update method signature

    List<EventDTO> getAllEventsExceptMine(Long userId);

    List<EventDTO> getMyEvents(Long userId);

}
