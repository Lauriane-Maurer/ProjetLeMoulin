package fr.simplon.projetlemoulin.clientcontroller;

import fr.simplon.projetlemoulin.entities.Event;
import fr.simplon.projetlemoulin.entities.Partner;
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



    /**
     * Retrieves an event by its id.
     *
     * @param model The model used to add the retrieved event.
     * @param id The identifier of the event to retrieve.
     * @return The name of the view to display for the event details.
     */
    @GetMapping("/evenements/{id}")
    public String getEventById(Model model, @PathVariable Long id) {
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8085/rest/events/{id}";
        ResponseEntity<Event> response = restTemplate.getForEntity(url, Event.class, id);
        Event event = response.getBody();
        model.addAttribute("event", event);
        return "fiche_evenement";
    }


    /**
     * Displays the management list of events for the admin.
     *
     * @param model The model used to add the list of events to be displayed.
     * @return The name of the view to show the administration list of events.
     */
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


    /**
     * Displays the event creation form.
     *
     * @param model The model used to add the event object and the list of partners to be displayed for selection in the form.
     * @return The name of the view to show the event creation form.
     */
    @GetMapping("/admin/creationEvenement")
    public String displayEventForm(Model model) {
        this.restTemplate = new RestTemplate();
        Event event = new Event();
        model.addAttribute("event", event);
        String url = "http://localhost:8085/rest/partners";
        ResponseEntity<List<Partner>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Partner>>() {
                });
        List<Partner> partners = response.getBody();
        model.addAttribute("partners", partners);
        return "admin/formulaireEvenement";
    }


    /**
     * Adds a new event by processing the event creation form submission.
     *
     * @param event The event object to be added, obtained from the form data.
     * @param bindingResult The binding result object that holds the validation errors, if any.
     * @param model The model used to add attributes for the view.
     * @return The name of the view to display a success message if the event is created, or to show the event creation form with validation errors.
     */
    @PostMapping("/admin/creationEvenement")
    public String addEvent(@ModelAttribute("event") @Valid Event event, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/formulaireEvenement";
        } else {
            this.restTemplate = new RestTemplate();
            String url = "http://localhost:8085/rest/events";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Event> request = new HttpEntity<>(event, headers);
            ResponseEntity<Event> response = restTemplate.postForEntity(url, request, Event.class);

            model.addAttribute("Message", "L'évènement a bien été créé.");
            return "message";
        }
    }


    /**
     * Displays the form for updating an existing event.
     *
     * @param model The model used to add the event object and the list of partners to be displayed for selection in the form.
     * @param id The identifier of the event to be updated.
     * @return The name of the view to show the event modification form.
     */
    @GetMapping("/admin/formulaireModificationEvenement/{id}")
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


    /**
     * Updates an existing event by processing the event modification form submission.
     *
     * @param event The event object with updated data obtained from the form.
     * @param bindingResult The binding result object that holds the validation errors, if any.
     * @param id The identifier of the event to be updated.
     * @param model The model used to add attributes for the view.
     * @return The name of the view to display a success message if the event is updated, or to show the event modification form with validation errors.
     */
    @PutMapping("/admin/ModificationEvenement/{id}")
    public String updateEvent(@ModelAttribute("event") @Valid Event event, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/formulaireModifEvenement";
        } else {
            this.restTemplate = new RestTemplate();
            String url = "http://localhost:8085/rest/UpdateEvent/{id}";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Event> request = new HttpEntity<>(event, headers);
            ResponseEntity<Event> response = restTemplate.exchange(url, HttpMethod.PUT, request, Event.class, id);

            model.addAttribute("Message", "Les mises à jour de l'évènement ont bien été enregistrées.");
            return "message";
        }
    }


    /**
     * Deletes an event by its identifier.
     *
     * @param model The model used to add attributes for the view.
     * @param id The identifier of the event to be deleted.
     * @return The name of the view to display a success message after deleting the event.
     */
    @GetMapping ("/admin/supprimerEvenement/{id}")
    public String deleteEvent(Model model, @PathVariable Long id){
        this.restTemplate = new RestTemplate();
        String url="http://localhost:8085/rest/events/{id}";
        restTemplate.delete(url, id);

        model.addAttribute("Message", "L'évènement a bien été supprimé.");
        return "message";
    }


}
