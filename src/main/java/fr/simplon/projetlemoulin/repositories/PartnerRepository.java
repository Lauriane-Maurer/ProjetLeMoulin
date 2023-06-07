package fr.simplon.projetlemoulin.repositories;

import fr.simplon.projetlemoulin.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository <Partner, Long> {
}
