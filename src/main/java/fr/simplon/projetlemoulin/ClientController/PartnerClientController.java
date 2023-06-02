package fr.simplon.projetlemoulin.ClientController;

import fr.simplon.projetlemoulin.Entities.Partner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Controller class for handling client-side partner-related requests.
 *
 * This class acts as a controller for the client-side application and handles
 * requests related to partners.
 *
 */

@Controller
public class PartnerClientController {

    private RestTemplate restTemplate;

    /**
     * Displays the Territoire page.
     *
     * This method handles the GET request for the "/territoire" endpoint and retrieves
     * a list of partners from the REST API. The partners are then added to the model
     * attribute "partners". The method returns the view name "territoire" to render
     * the Territoire page.
     *
     * @param model The model object to add attributes for the view.
     * @return The view name "territoire".
     */
    @GetMapping("/territoire")
    public String displayTerritoirePage(Model model){
        this.restTemplate = new RestTemplate();
        String url ="http://localhost:8085/rest/partners";
        ResponseEntity<List<Partner>> response=restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Partner>>(){});
        List<Partner> partners = response.getBody();
        model.addAttribute("partners", partners);
        return "territoire";
    }


    /**
     * Retrieves all partners for geolocation.
     *
     * This method sends a GET request to the "/geolocalisationPartenaires" endpoint to
     * retrieve a list of partners with geolocation information. The partners are returned
     * as a JSON array. The method is intended to be used in JavaScript file "partnersMap.js" to populate a map
     * with partner locations.
     *
     * @return A JSON array of partners with geolocation information.
     */
    @GetMapping("/geolocalisationPartenaires")
    @ResponseBody
    public List<Partner> getAllPartnersAPIGeolocalisation() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<List<Partner>> response = restTemplate.exchange("http://localhost:8085/rest/partnersLocation", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Partner>>() {});
        return response.getBody();
    }


    @GetMapping("/formulairePartenaire")
    public String displayPartnerForm(Model model) {
        this.restTemplate = new RestTemplate();
        Partner partner = new Partner();
        model.addAttribute("partner", partner);
        return "admin/formulairePartenaire";
    }

    @PostMapping("/AjouterPartenaire")
    public String addPartner(@ModelAttribute("partner") Partner partner, Model model) {
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8085/rest/partners";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Partner> request = new HttpEntity<>(partner, headers);
        ResponseEntity<Partner> response = restTemplate.postForEntity(url, request, Partner.class);

        model.addAttribute("Message", "L'organisme partenaire a bien été ajouté.");
        return "message";
    }


    @GetMapping("/admin/listePartenaires")
    public String displayPartnersListAdmin(Model model){
        this.restTemplate = new RestTemplate();
        String url ="http://localhost:8085/rest/partners";
        ResponseEntity<List<Partner>> response=restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Partner>>(){});
        List<Partner> partners = response.getBody();
        model.addAttribute("partners", partners);
        return "admin/listePartenairesAdmin";
    }


    @GetMapping("/InfoPartenaire/{id}")
    public String displayUpdatePartnerForm(Model model, @PathVariable Long id){
        this.restTemplate = new RestTemplate();
        String url="http://localhost:8085/rest/partners/{id}";
        ResponseEntity<Partner> response = restTemplate.getForEntity(url, Partner.class, id);
        Partner partner = response.getBody();
        model.addAttribute("partner", partner);
        return "admin/formulaireModifPartenaire";
    }

    @PostMapping("MAJPartenaire/{id}")
    public String updatePartner (@ModelAttribute("organisme") Partner partner, @PathVariable Long id, Model model) {
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8085/rest/updatePartner/{id}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Partner> request = new HttpEntity<>(partner, headers);
        ResponseEntity<Partner> response = restTemplate.exchange(url, HttpMethod.PUT, request, Partner.class, id);

        model.addAttribute("Message", "Les données de l'organisme partenaire ont bien été mises à jour.");
        return "message";
    }


    @GetMapping ("supprimerPartenaire/{id}")
    public String deletePartner(Model model, @PathVariable Long id){
        this.restTemplate = new RestTemplate();
        String url="http://localhost:8085/rest/partners/{id}";
        restTemplate.delete(url, id);

        model.addAttribute("Message", "L'organisme partenaire a bien été supprimé.");
        return "message";
    }

}
