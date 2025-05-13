package com.SportSync2.service;

import com.SportSync2.dto.SubEventDTO;
import com.SportSync2.entity.SubEvent;

import java.util.List;

public interface SubEventService {
    public void createSubEvents(Long eventId, List<SubEventDTO> subEventDTOs);
    List<SubEventDTO> getSubEventsByEventId(Long eventId);
    //List<SubEventDTO> getSubEvents(Long eventId);

    SubEvent addSubEvent(Long eventId, SubEventDTO subEventDTO);
}

