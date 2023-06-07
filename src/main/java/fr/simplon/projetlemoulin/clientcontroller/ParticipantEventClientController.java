package fr.simplon.projetlemoulin.clientcontroller;

import fr.simplon.projetlemoulin.entities.Event;
import fr.simplon.projetlemoulin.entities.Participant;
import fr.simplon.projetlemoulin.entities.ParticipantEvent;
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


    /**
     * Displays the form for event participation.
     *
     * @param model The model used to add the participantEvent, event, and participant objects
     * to be displayed in the form.
     * @param eventId The ID of the event for which participation is being registered.
     * @param username The username of the participant registering for the event.
     * @return The name of the view to show the event participation form if the event is
     * available and the participant exists, or to redirect to the participant information
     * registration form if the participant does not exist, or to show a message if the event
     * is already full.
     */
    @GetMapping("/membre/InscriptionParticipant/{eventId}/{username}")
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
                return "redirect:/membre/InfoParticipant/{username}";
            }
        }else {
            model.addAttribute("Message", "Cet évènement est déjà complet.");
            return "message";
        }
    }


    /**
     * Registers a participant for an event by processing the participant event registration
     * form submission.
     * If the participant is not already registered for the event, their registration is
     * recorded and the number of available places for the event is decremented by 1.
     *
     * @param participantEvent The participant event object containing the participant and
     * event information obtained from the form.
     * @param model The model used to add attributes for the view.
     * @return The name of the view to display a success message after registering the
     * participant for the event, or to show a message if the participant is already registered
     * for the event.
     */
    @PostMapping("/membre/InscriptionParticipant")
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


    /**
     * Displays the list of event participations for a specific participant.
     *
     * @param model The model used to add attributes for the view.
     * @param username The username of the participant.
     * @return The name of the view to display the list of event participations if the participant exists and has participations, or to show a message if the participant has no current event participations.
     */
    @GetMapping("/membre/ListeParticipations/{username}")
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
            model.addAttribute("Message", "Vous n'êtes actuellement inscrit à aucun évènement. Pour participer à un évènement, rendez-vous sur la page 'Programmation' dans la barre de navigation !");
            return "message";
        }
    }


    /**
     * Deletes a participation of a participant from an event.
     *
     * @param model The model used to add attributes for the view.
     * @param id The ID of the participation to be deleted.
     * @return The name of the view to display a message confirming the cancellation of the participation.
     */
    @GetMapping("/membre/annulationParticipation/{id}")
    public String deleteParticipation(Model model, @PathVariable Long id){
        this.restTemplate = new RestTemplate();
        String url="http://localhost:8085/rest/participantEvent/{id}";
        restTemplate.delete(url, id);

        model.addAttribute("Message", "Votre inscription à cet évènement a bien été annulée. Retrouvez la liste des évènements auxquels vous êtes inscrit.e en cliquant sur 'Mes évènements' dans la barre de navigation. ");
        return "message";
    }


    /**
     * Displays the list of participants registered for an event.
     *
     * @param model The model used to add attributes for the view.
     * @param eventId The ID of the event for which the list of participants is to be displayed.
     * @return The name of the view to display the list of participants registered for the event.
     */
    @GetMapping("/admin/InscriptionsEvenement/{eventId}")
    public String displayParticipantsEvent(Model model, @PathVariable Long eventId) {
        this.restTemplate = new RestTemplate();
        String EventUrl = "http://localhost:8085/rest/events/{id}";
        ResponseEntity<Event> response1 = restTemplate.exchange(
                EventUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Event>() {},
                eventId
        );
        Event event = response1.getBody();
        model.addAttribute("event", event);

        String url = "http://localhost:8085/rest/ParticipantsEvent/{eventId}";
        ResponseEntity<List<ParticipantEvent>> response2 = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ParticipantEvent>>() {},
                eventId
        );
        List<ParticipantEvent> participantEvents = response2.getBody();
        model.addAttribute("participantEvents", participantEvents);
        return "admin/listeInscritsEvenement";
    }

}
