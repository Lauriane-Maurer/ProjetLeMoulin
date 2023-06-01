package fr.simplon.projetlemoulin.RestController;
import fr.simplon.projetlemoulin.Entities.Partner;
import fr.simplon.projetlemoulin.Repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


/**
 * REST controller for managing partners.
 *
 * This class handles HTTP requests related to partner entities. It provides
 * endpoints for retrieving, adding, updating, and deleting partners (CRUD).
 *
 *  @RestController indicates that this class is a controller that handles RESTful requests
 */

@RestController
public class PartnerRestController {

    private PartnerRepository repo;


    @Autowired
    public PartnerRestController(PartnerRepository fr) {
        this.repo = fr;

        //this.repo.save(new Partner("Gaec Gwel Ar Mor", "producteur","	Elevage de vaches, production et vente sur les marchés et à la ferme de produits laitiers: beurre, crème, yaourt à boire, petits fromages frais enrobés ou nature, skir.", "7 Bourg", "Lampaul-Ploudalmezeau", 29830, "www.gwelarmor.fr", 48.56241900228485, -4.609485545551342));
        //this.repo.save(new Partner("Kanevedenn", "agriculture", "Kanevedenn est un jardin maraicher Bio créé par Mathieu et Marilyn à Lampaul-Ploudalmézeau en 2007. Création de panier en ligne à récupérer en point de retrait ou livraison.", "15 route de Kervizin", "Lampaul-Ploudalmezeau", 29830, "www.maboutiquefermiere.fr", 48.56466226388162, -4.647926511065619));
        //this.repo.save(new Partner("L'atelier de Viliv", "artisanat", "Créatrice de décors sur céramique, d’objets de décoration et de bijoux fantaisie, à partir de différents matériaux (pâte polymère, cuir, papier, résine …).", "3 rue de la fontaine", "Coat-Méal", 29870, "www.latelierdeviliv.fr", 48.5186046346549, -4.540717612891484));
        //this.repo.save(new Partner("Du haut de ma dune", "artisanat", "Création de bijoux originaux en cuir de poisson: pendentifs, boucles d’oreilles et bagues, à découvrir en boutique!", "130 Ar lud", "Landéda", 29870, "www.duhautdemadune.com", 48.61263611216837, -4.557234647722291));
        //this.repo.save(new Partner("Théatre à Molette", "animation", "Le théâtre à molette est une compagnie de théâtre d’objets, léger et compact, capable d’aller n’importe où (enfin presque !). Des histoires et des mots pour des spectacles toujours plus originaux.", "6 rue Mézou Kervidré", "Le Conquet", 29217, "www.theatreamolette.fr", 48.35226286118822, -4.762655971098742));
        //this.repo.save(new Partner("Parc Naturel Marin d'Iroise", "environnement", "Au bout du Finistère : là où finit la terre commence la mer d’Iroise. Depuis 2007, le Parc naturel marin d’Iroise veille sur cette mer mythique. Il protège les habitats et assure la cohésion des activités économiques sur plus de 3500 km2.", "23 rue radio", "Le Conquet", 29217, "www.parc-marin-iroise.fr", 48.35482736547393, -4.779469303808932));
        //this.repo.save(new Partner("Bergerie des abers", "agriculture", "Elodie et Ronan élèvent plus de 240 brebis laitières de race lacaune sur 24ha de pâturage. Fabrication de produits laitiers, découpe de viande, vente en circuit court, à la ferme, sur les marchés et dans les magasins de produits locaux.", "Gorréquéar", "Plouvien", 29860, "www.bergeriedesabers.fr",48.540765697781275, -4.522247283475145));
        //this.repo.save(new Partner("Ar Vro Bagan", "animation", "Ar Vro Bagan est une troupe de théâtre professionnelle en langue bretonne, crée en 1965, basée à Plouguerneau. Elle propose des cours breton, des stages de théâtre pour enfants et ados, et des cours de chants et de danses bretonnes.","ZA du Hellez", "Plouguerneau", 29880, "www.arvrobagan.bzh",48.62956014028035, -4.484334853087361));
        //this.repo.save(new Partner("Brasserie des abers", "artisanat", "La Brasserie des Abers est une brasserie artisanale. Elle est située à Ploudalmézeau près de Brest. La brasserie brasse onze bières de fermentation haute, non filtrées et non pasteurisées.", "2 avenue de Portsall", "Ploudalmézeau", 29830, "www.facebook.com/brasseriedesabers/",48.56085564281277, -4.630583807123573));

    }

    /**
     * Retrieves all partners from the repository.
     *
     * @return A list of all partners.
     */
    @GetMapping(path = "/rest/partners")
    public List<Partner> getAllPartners() {
        return repo.findAll();
    }


    /**
     * Retrieves all partners as JSON from the repository.
     *
     * @return A list of all partners in JSON format.
     */
    @GetMapping("/rest/partnersLocation")
    @ResponseBody
    public List<Partner> getAllPartnersAsJson() {
        List<Partner> partners = repo.findAll();
        return partners;
    }


    /**
     * Retrieves details of a specific partner by its ID.
     *
     * @param id The ID of the partner to retrieve.
     * @return The partner with the specified ID.
     * @throws NoSuchElementException if the partner with the given ID is not found.
     */
    @GetMapping(path = "/rest/partners/{id}")
    public Partner getPartnerDetails(@PathVariable Long id)throws NoSuchElementException {
        return repo.findById(id).orElseThrow();
    }


    /**
     * Adds a new partner to the repository.
     *
     * @param newPartner The partner to add.
     * @return The newly added partner.
     */
    @PostMapping("/rest/partners")
    public Partner addPartner(@RequestBody Partner newPartner) {
        return repo.save(newPartner);
    }


    /**
     * Updates an existing partner in the repository.
     *
     * @param newPartner The updated partner data.
     * @param id The ID of the partner to update.
     * @return The updated partner.
     */
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
                    partner.setEvent(newPartner.getEvent());
                    return repo.save(partner);
                })
                .orElseGet(() -> {
                    newPartner.setId(id);
                    return repo.save(newPartner);
                });
    }

    /**
     * Deletes a partner from the repository.
     *
     * @param id The ID of the partner to delete.
     */
    @DeleteMapping("/rest/partners/{id}")
    public void deletePartner(@PathVariable Long id) {
        repo.deleteById(id);
    }

}

