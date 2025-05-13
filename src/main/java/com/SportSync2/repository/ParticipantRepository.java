package com.SportSync2.repository;

import com.SportSync2.entity.Participant;
import com.SportSync2.entity.SubEvent;
import com.SportSync2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByUserAndSubEvent(User user, SubEvent subEvent);
}
