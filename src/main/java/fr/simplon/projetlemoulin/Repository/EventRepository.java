package fr.simplon.projetlemoulin.Repository;

import fr.simplon.projetlemoulin.Entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
