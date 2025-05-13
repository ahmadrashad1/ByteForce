package com.SportSync2.repository;

import com.SportSync2.entity.Ground;
import com.SportSync2.entity.GroundBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface GroundBookingRepository extends JpaRepository<GroundBooking, Long> {

    List<GroundBooking> findByGroundIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long groundId, LocalDateTime end, LocalDateTime start
    );

    List<GroundBooking> findByUserId(Long userId);

    void deleteByGroundIdAndStartTimeAndEndTime(Long groundId, LocalDateTime startTime, LocalDateTime endTime);


}

