package fr.simplon.projetlemoulin.Repository;

import fr.simplon.projetlemoulin.Entities.ParticipantEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantEventRepository extends JpaRepository<ParticipantEvent, Long> {
}
