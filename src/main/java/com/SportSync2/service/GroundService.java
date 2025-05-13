package com.SportSync2.service;

import com.SportSync2.dto.GroundDTO;
import com.SportSync2.entity.Ground;
import com.SportSync2.repository.GroundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroundService {

    private final GroundRepository groundRepo;

    public List<GroundDTO> getAllGrounds() {
        return groundRepo.findAll().stream()
                .map(ground -> GroundDTO.builder()
                        .id(ground.getId())
                        .name(ground.getName())
                        .location(ground.getLocation())
                        .build())
                .collect(Collectors.toList());
    }
}

