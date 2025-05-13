package com.SportSync2.service;

import com.SportSync2.dto.GroundDTO;
import com.SportSync2.entity.Ground;
import com.SportSync2.repository.GroundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroundServiceTest {

    @Mock
    private GroundRepository groundRepo;

    @InjectMocks
    private GroundService groundService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGrounds_ReturnsListOfGroundDTOs() {
        // Arrange
        Ground ground1 = new Ground(1L, "Football Field", "Campus A");
        Ground ground2 = new Ground(2L, "Cricket Ground", "Campus B");

        when(groundRepo.findAll()).thenReturn(Arrays.asList(ground1, ground2));

        // Act
        List<GroundDTO> groundDTOs = groundService.getAllGrounds();

        // Assert
        assertEquals(2, groundDTOs.size());

        GroundDTO dto1 = groundDTOs.get(0);
        assertEquals("Football Field", dto1.getName());
        assertEquals("Campus A", dto1.getLocation());

        GroundDTO dto2 = groundDTOs.get(1);
        assertEquals("Cricket Ground", dto2.getName());
        assertEquals("Campus B", dto2.getLocation());

        verify(groundRepo, times(1)).findAll();
    }
}
