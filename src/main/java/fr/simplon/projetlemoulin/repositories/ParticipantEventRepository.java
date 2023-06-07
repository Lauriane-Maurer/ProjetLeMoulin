package fr.simplon.projetlemoulin.repositories;

import fr.simplon.projetlemoulin.entities.ParticipantEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantEventRepository extends JpaRepository<ParticipantEvent, Long> {

    List<ParticipantEvent> findByParticipantId(Long participantId);
    List<ParticipantEvent> findByEventId(Long eventId);

    boolean existsByParticipantIdAndEventId(Long participantId, Long evenementId);
}
