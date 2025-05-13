package com.SportSync2.repository;

import com.SportSync2.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
   // List<Event> findByCreatorIdNot(Long creatorId);
    List<Event> findByHost_IdNot(Long hostId);
    List<Event> findByHost_Id(Long userId);

    //List<Event> findByCreatedBy_Id(Long userId);


    //List<Event> findByHostIdNot(Long userId);
}