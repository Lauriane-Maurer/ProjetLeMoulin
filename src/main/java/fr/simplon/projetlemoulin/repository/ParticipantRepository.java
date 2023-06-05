package fr.simplon.projetlemoulin.repository;

import fr.simplon.projetlemoulin.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Participant findByUsername(String username);

}
