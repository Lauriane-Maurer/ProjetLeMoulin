package fr.simplon.projetlemoulin.ClientController;

import fr.simplon.projetlemoulin.Entities.Event;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class EventClientController {

    private RestTemplate restTemplate;
    @GetMapping("/programmation")
    public String getAllEvents(Model model) {
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8085/rest/events";
        ResponseEntity<List<Event>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Event>>() {
                });
        List<Event> events = response.getBody();
        model.addAttribute("events", events);
        return "programmation";
    }


}
