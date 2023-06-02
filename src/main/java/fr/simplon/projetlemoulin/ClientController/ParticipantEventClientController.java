package fr.simplon.projetlemoulin.ClientController;

import fr.simplon.projetlemoulin.Entities.Event;
import fr.simplon.projetlemoulin.Entities.Participant;
import fr.simplon.projetlemoulin.Entities.ParticipantEvent;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class ParticipantEventClientController {

    private RestTemplate restTemplate;

    @GetMapping("/InscriptionParticipant/{eventId}/{username}")
    public String displayEventParticipationForm(Model model, @PathVariable Long eventId, @PathVariable String username) {
        ParticipantEvent participantEvent = new ParticipantEvent();
        model.addAttribute("participantEvent", participantEvent);
        this.restTemplate = new RestTemplate();
        String eventUrl = "http://localhost:8085/rest/events/{id}";
        ResponseEntity<Event> eventResponse = restTemplate.getForEntity(eventUrl, Event.class, eventId);
        Event event = eventResponse.getBody();

        if ((event.getAvailable_places() != null) && (event.getAvailable_places() != 0) ) {
            model.addAttribute("event", event);
            participantEvent.setEvent(event);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            username = authentication.getName();
            String url = "http://localhost:8085/rest/participants/{username}";
            ResponseEntity<Participant> response = restTemplate.getForEntity(url, Participant.class, username);
            Participant participant = response.getBody();
            if (participant != null) {
                model.addAttribute("participant", participant);
                participantEvent.setParticipant(participant);
                return "membres/formulaireParticipationEvenement";
            } else {
                return "redirect:/InfoParticipant/{username}";
            }
        }else {
            model.addAttribute("Message", "Cet évènement est déjà complet.");
            return "message";
        }
    }


    @PostMapping("/InscriptionParticipant")
    public String registrationParticipantEvent(@ModelAttribute("participantEvent") ParticipantEvent participantEvent, Model model) {
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8085/rest/participantEvent";
        Participant participant = participantEvent.getParticipant();
        Event event = participantEvent.getEvent();

        Long participantId = participant.getId();
        Long eventId = event.getId();
        String checkRegistrationUrl = "http://localhost:8085/rest/participantEvent/checkRegistration?participantId=" + participantId + "&eventId=" + eventId;
        ResponseEntity<Boolean> responseCheck = restTemplate.getForEntity(checkRegistrationUrl, Boolean.class);
        if (responseCheck.getBody()) {
            model.addAttribute("Message", "Vous êtes déjà inscrit à cet évènement.");
            return "message";
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ParticipantEvent> request = new HttpEntity<>(participantEvent, headers);
            ResponseEntity<ParticipantEvent> response = restTemplate.postForEntity(url, request, ParticipantEvent.class);

            String decrementAvailablePlacesUrl = "http://localhost:8085/rest/DecrementAvailablePlaces/{id}";
            HttpHeaders headers2 = new HttpHeaders();
            headers2.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Event> request2 = new HttpEntity<>(event, headers);
            ResponseEntity<Event> response2 = restTemplate.exchange(decrementAvailablePlacesUrl, HttpMethod.POST, request, Event.class, eventId);

            model.addAttribute("Message", "Votre inscription a bien été enregistrée. Retrouvez la liste des évènements auxquels vous êtes inscrit.e en cliquant sur 'Mes évènements' dans la barre de navigation.");
            return "message";
        }
    }


    @GetMapping("/ListeParticipations/{username}")
    public String displayParticipationsList(Model model, @PathVariable String username) {
        this.restTemplate = new RestTemplate();
        String participantUrl = "http://localhost:8085/rest/participants/{username}";
        ResponseEntity<Participant> response1 = restTemplate.getForEntity(participantUrl, Participant.class, username);
        Participant participant = response1.getBody();
        model.addAttribute("participant", participant);
        if (participant != null) {
            String url = "http://localhost:8085/rest/participantEvent/{participantId}";
            ResponseEntity<List<ParticipantEvent>> response2 = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ParticipantEvent>>() {},
                    participant.getId()
            );
            List<ParticipantEvent> participantEvents = response2.getBody();
            model.addAttribute("participantEvents", participantEvents);
            return "membres/listeParticipations";
        } else {
            return "/programmation";
        }
    }

    @GetMapping("annulationParticipation/{id}")
    public String deleteParticipation(Model model, @PathVariable Long id){
        this.restTemplate = new RestTemplate();
        String url="http://localhost:8085/rest/participantEvent/{id}";
        restTemplate.delete(url, id);

        model.addAttribute("Message", "Votre inscription à cet évènement a bien été annulée. Retrouvez la liste des évènements auxquels vous êtes inscrit.e en cliquant sur 'Mes évènements' dans la barre de navigation. ");
        return "message";
    }

}
