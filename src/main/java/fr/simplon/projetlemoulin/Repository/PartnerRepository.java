package fr.simplon.projetlemoulin.Repository;

import fr.simplon.projetlemoulin.Entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository <Partner, Long> {
}
