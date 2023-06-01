package fr.simplon.projetlemoulin.Repository;

import fr.simplon.projetlemoulin.Entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Participant findByUsername(String username);

}
