package com.SportSync2.service;

import com.SportSync2.dto.SubEventDTO;
import com.SportSync2.entity.Event;
import com.SportSync2.entity.GroundBooking;
import com.SportSync2.entity.SubEvent;
import com.SportSync2.entity.Ground;
import com.SportSync2.mapper.SubEventMapper;
import com.SportSync2.repository.EventRepository;
import com.SportSync2.repository.SubEventRepository;
import com.SportSync2.repository.GroundRepository;
import com.SportSync2.repository.GroundBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubEventServiceImpl implements SubEventService {

    private final SubEventRepository subEventRepository;
    private final EventRepository eventRepository;
    private final SubEventMapper subEventMapper;


    private final GroundRepository groundRepository;

    //@Autowired
    private final GroundBookingRepository groundBookingRepository;

    public void createSubEvents(Long eventId, List<SubEventDTO> subEventDTOs) {

        if (subEventDTOs == null) {
            throw new IllegalArgumentException("SubEventDTOs DTOs cannot be null");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        for (SubEventDTO dto : subEventDTOs) {
            Ground ground = groundRepository.findById(dto.getGroundId())
                    .orElseThrow(() -> new RuntimeException("Ground not found with ID: " + dto.getGroundId()));

            // Check availability
            boolean isBooked = !groundBookingRepository.findByGroundIdAndStartTimeLessThanAndEndTimeGreaterThan(
                    ground.getId(),
                    dto.getEndTime(),
                    dto.getStartTime()
            ).isEmpty();

            if (isBooked) {
                throw new RuntimeException("Ground is already booked for sub-event " + dto.getName());
            }

            // Create booking
            GroundBooking booking = GroundBooking.builder()
                    .ground(ground)
                    .user(event.getHost()) // or whoever is responsible
                    .startTime(dto.getStartTime())
                    .endTime(dto.getEndTime())
                    .isEventBooking(true)
                    .build();
            groundBookingRepository.save(booking);

            // Create sub-event
            SubEvent subEvent = subEventMapper.toEntity(dto);
            subEvent.setEvent(event);
            subEvent.setGround(ground);
            subEvent.setBooking(booking);

            subEventRepository.save(subEvent);
        }
    }


    @Override
    public List<SubEventDTO> getSubEventsByEventId(Long eventId) {

        if(eventId == null){
            throw new IllegalArgumentException("event id cannot be null");
        }
        List<SubEvent> subEvents = subEventRepository.findByEventId(eventId);
        return subEvents.stream()
                .map(subEventMapper::toDto)
                .collect(Collectors.toList());
    }



    @Override
    public SubEvent addSubEvent(Long eventId, SubEventDTO subEventDTO) {

        if(eventId == null){
            throw new IllegalArgumentException("event id cannot be null");
        }
        if (subEventDTO == null) {
            throw new IllegalArgumentException("SubEventDTO cannot be null");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        SubEvent subEvent = subEventMapper.toEntity(subEventDTO);
        subEvent.setEvent(event);

        return subEventRepository.save(subEvent);
    }
}
