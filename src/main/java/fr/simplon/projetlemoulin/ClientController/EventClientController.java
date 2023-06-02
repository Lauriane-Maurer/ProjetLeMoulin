package fr.simplon.projetlemoulin.ClientController;

import fr.simplon.projetlemoulin.Entities.Event;
import fr.simplon.projetlemoulin.Entities.Partner;
import jakarta.validation.Valid;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Controller class for handling client-side event-related requests.
 *
 * This class acts as a controller for the client-side application and handles
 * requests related to events.
 *
 */

@Controller
public class EventClientController {

    private RestTemplate restTemplate;


    /**
     * Retrieves the "programmation" page.
     *
     * This method handles the GET request for the "/programmation" endpoint and
     * returns the page "programmation". It is responsible for displaying
     * the "programmation" page.
     *
     * @return The view name for the "programmation" page.
     */
    @GetMapping("/programmation")
    public String getProgrammationPage() {
        return "programmation";
    }


    /**
     * Retrieves all events from the API endpoint.
     *
     * This method handles the GET request for the "/cartesEvenements" endpoint and retrieves
     * all event from the API. It uses RestTemplate to send an HTTP GET request to the
     * API endpoint "http://localhost:8085/rest/eventsCards". The response, containing a list
     * of events, is then returned as the result to be displayed in card format using the
     * "EventCard" javascript file.
     *
     * @return A list of events retrieved from the API.
     */
    @GetMapping("/cartesEvenements")
    @ResponseBody
    public List<Event> getAllEventsCards() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<List<Event>> response = restTemplate.exchange("http://localhost:8085/rest/eventsCards", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Event>>() {
        });
        return response.getBody();
    }

    @GetMapping("/evenements/{id}")
    public String getEventById(Model model, @PathVariable Long id) {
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8085/rest/events/{id}";
        ResponseEntity<Event> response = restTemplate.getForEntity(url, Event.class, id);
        Event event = response.getBody();
        model.addAttribute("event", event);
        return "fiche_evenement";
    }



    @GetMapping("/admin/listeEvenements")
    public String displayEventsManagementList(Model model){
        this.restTemplate = new RestTemplate();
        String url ="http://localhost:8085/rest/events";
        ResponseEntity<List<Event>> response=restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Event>>(){});
        List<Event> events = response.getBody();
        model.addAttribute("events", events);
        return "admin/listeEvenementsAdmin";
    }


    @GetMapping("/formulaireModificationEvenement/{id}")
    public String displayUpdateEventForm(Model model, @PathVariable Long id){
        this.restTemplate = new RestTemplate();
        String url="http://localhost:8085/rest/events/{id}";
        ResponseEntity<Event> response = restTemplate.getForEntity(url, Event.class, id);
        Event event = response.getBody();
        model.addAttribute("event", event);
        String url2 = "http://localhost:8085/rest/partners";
        ResponseEntity<List<Partner>> response2 = restTemplate.exchange(
                url2,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Partner>>() {
                });
        List<Partner> partners = response2.getBody();
        model.addAttribute("partners", partners);
        return "admin/formulaireModifEvenement";
    }


    @PostMapping("ModificationEvenement/{id}")
    public String updateEvent (@ModelAttribute("event") @Valid Event event, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            // Gérer les erreurs de validation ici
            return "admin/formulaireModifEvenement";
        } else {
            this.restTemplate = new RestTemplate();
            String url = "http://localhost:8085/rest/UpdateEvent/{id}";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Event> request = new HttpEntity<>(event, headers);
            ResponseEntity<Event> response = restTemplate.exchange(url, HttpMethod.POST, request, Event.class, id);

            model.addAttribute("Message", "Les mises à jour de l'évènement ont bien été enregistrées.");
            return "message";
        }
    }

}
