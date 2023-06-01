package fr.simplon.projetlemoulin.ClientController;

import fr.simplon.projetlemoulin.Entities.Participant;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class ParticipantClientController {

    private RestTemplate restTemplate;

    @GetMapping("/InfoParticipant/{username}")
    public String displayParticipantInformationsForm(Model model, @PathVariable String username) {
        this.restTemplate = new RestTemplate();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        Participant participant = new Participant();
        participant.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("participant", participant);
        return "membres/formulaireInfoParticipant";
    }


    @PostMapping("/InfoParticipant")
    public String registerInfoParticipant(@ModelAttribute("participant") Participant participant) {
        this.restTemplate = new RestTemplate();

        // Enregistrer le participant
        String Url = "http://localhost:8085/rest/participants";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Participant> participantRequest = new HttpEntity<>(participant, headers);
        ResponseEntity<Participant> participantResponse = restTemplate.postForEntity(Url, participantRequest, Participant.class);
        return "redirect:/programmation";
    }

}
