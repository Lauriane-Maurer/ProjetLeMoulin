package fr.simplon.projetlemoulin.repositories;

import fr.simplon.projetlemoulin.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
