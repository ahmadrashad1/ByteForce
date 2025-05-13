package com.SportSync2.repository;

import com.SportSync2.entity.Ground;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface GroundRepository extends JpaRepository<Ground, Long> {
}
