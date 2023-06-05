package fr.simplon.projetlemoulin.repository;

import fr.simplon.projetlemoulin.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository <Partner, Long> {
}
