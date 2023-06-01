package fr.simplon.projetlemoulin.ClientController;

import fr.simplon.projetlemoulin.Entities.Event;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public String getEvent(Model model, @PathVariable Long id) {
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8085/rest/events/{id}";
        ResponseEntity<Event> response = restTemplate.getForEntity(url, Event.class, id);
        Event event = response.getBody();
        model.addAttribute("event", event);
        return "fiche_evenement";
    }

}
