package com.SportSync2.service;

import com.SportSync2.dto.EventDTO;
import com.SportSync2.entity.Event;
import com.SportSync2.entity.Ground;
import com.SportSync2.entity.GroundBooking;
import com.SportSync2.entity.User;
import com.SportSync2.mapper.EventMapper;
import com.SportSync2.repository.EventRepository;
import com.SportSync2.repository.GroundBookingRepository;
import com.SportSync2.repository.GroundRepository;
import com.SportSync2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final GroundRepository groundRepository;
    private final GroundBookingRepository groundBookingRepository;

    @Transactional
    @Override
    public Event createEvent(EventDTO eventDTO, Long hostId) {
        if (eventDTO == null) {
            throw new NullPointerException("EventDTO cannot be null");
        }

        User host = userRepository.findById(hostId)
                .orElseThrow(() -> new RuntimeException("Host not found with ID: " + hostId));

        Event event = eventMapper.toEntity(eventDTO);
        event.setHost(host);
        event.setCreatedBy(host);

        return eventRepository.save(event);
    }


    @Override
    public List<EventDTO> getAllEventsExceptMine(Long userId) {

        if (userId == null) {
            throw new NullPointerException("User ID cannot be null");
        }

        return eventRepository.findByHost_IdNot(userId)
                .stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<EventDTO> getMyEvents(Long userId) {

        if (userId == null) {
            throw new NullPointerException("User ID cannot be null");
        }

        return eventRepository.findByHost_Id(userId)
                .stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }



}
