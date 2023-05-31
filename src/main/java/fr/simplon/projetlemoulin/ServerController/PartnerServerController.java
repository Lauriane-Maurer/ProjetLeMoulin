import fr.simplon.projetlemoulin.Entities.Partner;
import fr.simplon.projetlemoulin.Repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class PartnerServerController {

    private PartnerRepository repo;


    @Autowired
    public PartnerServerController(PartnerRepository fr) {
        this.repo = fr;

        //this.repo.save(new Partner("Gaec Gwel Ar Mor", "producteur","7 Bourg", "Lampaul-Ploudalmezeau", 29830, "www.gwelarmor.fr", 48.56241900228485, -4.609485545551342));
        //this.repo.save(new Partner("Kanevedenn", "agriculture","15 route de Kervizin", "Lampaul-Ploudalmezeau", 29830, "www.maboutiquefermiere.fr", 48.56466226388162, -4.647926511065619));
        //this.repo.save(new Partner("L'atelier de Viliv", "artisanat","3 rue de la fontaine", "Coat-Méal", 29870, "www.latelierdeviliv.fr", 48.5186046346549, -4.540717612891484));
        //this.repo.save(new Partner("Du haut de ma dune", "artisanat","130 Ar lud", "Landéda", 29870, "www.duhautdemadune.com", 48.61263611216837, -4.557234647722291));
        //this.repo.save(new Partner("Théatre à Molette", "animation","6 rue Mézou Kervidré", "Le Conquet", 29217, "www.theatreamolette.fr", 48.35226286118822, -4.762655971098742));
        //.repo.save(new Partner("Parc Naturel Marin d'Iroise", "environnement","23 rue radio", "Le Conquet", 29217, "www.parc-marin-iroise.fr", 48.35482736547393, -4.779469303808932));
        //this.repo.save(new Partner("Bergerie des abers", "agriculture","Gorréquéar", "Plouvien", 29860, "www.bergeriedesabers.fr",48.540765697781275, -4.522247283475145));
        //this.repo.save(new Partner("Ar Vro Bagan", "animation","ZA du Hellez", "Plouguerneau", 29880, "www.arvrobagan.bzh",48.62956014028035, -4.484334853087361));
        //this.repo.save(new Partner("Brasserie des abers", "artisanat","2 avenue de Portsall", "Ploudalmézeau", 29830, "www.facebook.com/brasseriedesabers/",48.56085564281277, -4.630583807123573));

    }

    @GetMapping(path = "/rest/partners")
    public List<Partner> getAllPartners() {
        return repo.findAll();
    }


    @GetMapping("/rest/api/partners")
    @ResponseBody
    public List<Partner> getAllPartnersAsJson() {
        List<Partner> partners = repo.findAll();
        return partners;
    }


    @GetMapping(path = "/rest/partners/{id}")
    public Partner getPartnerDetails(@PathVariable Long id)throws NoSuchElementException {
        return repo.findById(id).orElseThrow();
    }


    @PostMapping("/rest/partners")
    public Partner addPartner(@RequestBody Partner newPartner) {
        return repo.save(newPartner);
    }


    @PutMapping("/rest/updatePartner/{id}")
    public Partner updatePartner(@RequestBody Partner newPartner, @PathVariable Long id) {
        return repo.findById(id)
                .map(partner -> {
                    partner.setName(newPartner.getName());
                    partner.setActivity(newPartner.getActivity());
                    partner.setDescription(newPartner.getDescription());
                    partner.setAdress(newPartner.getAdress());
                    partner.setTown(newPartner.getTown());
                    partner.setZip_code(newPartner.getZip_code());
                    partner.setUrl(newPartner.getUrl());
                    partner.setLatitude(newPartner.getLatitude());
                    partner.setLongitude(newPartner.getLongitude());
                    return repo.save(partner);
                })
                .orElseGet(() -> {
                    newPartner.setId(id);
                    return repo.save(newPartner);
                });
    }

    @DeleteMapping("/rest/partners/{id}")
    public void deletePartner(@PathVariable Long id) {
        repo.deleteById(id);
    }

}

