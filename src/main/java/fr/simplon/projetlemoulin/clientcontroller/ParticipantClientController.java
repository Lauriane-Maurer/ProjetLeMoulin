package fr.simplon.projetlemoulin.clientcontroller;

import fr.simplon.projetlemoulin.entities.Participant;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class ParticipantClientController {

    private RestTemplate restTemplate;


    /**
     * Displays the form for updating participant information.
     *
     * @param model The model used to add the participant object to be displayed in the form.
     * @param username The username of the participant whose information is being updated.
     * @return The name of the view to show the participant information modification form.
     */
    @GetMapping("/membre/InfoParticipant/{username}")
    public String displayParticipantInformationsForm(Model model, @PathVariable String username) {
        this.restTemplate = new RestTemplate();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        Participant participant = new Participant();
        participant.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("participant", participant);
        return "membres/formulaireInfoParticipant";
    }


    /**
     * Registers the participant information by processing the participant information form submission.
     *
     * @param participant The participant object with the updated information obtained from the form.
     * @param model The model used to add attributes for the view.
     * @return The name of the view to display a success message after registering the participant information.
     */
    @PostMapping("/membre/InfoParticipant")
    public String registerInfoParticipant(@ModelAttribute("participant") @Valid Participant participant, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "membres/formulaireInfoParticipant";
        } else {
        this.restTemplate = new RestTemplate();
        // Enregistrer le participant
        String Url = "http://localhost:8085/rest/participants";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Participant> participantRequest = new HttpEntity<>(participant, headers);
        ResponseEntity<Participant> participantResponse = restTemplate.postForEntity(Url, participantRequest, Participant.class);

        model.addAttribute("Message", "Vos coordonnées ont bien été enregistrées, vous pouvez maintenant vous inscrire à un évènement! Pour modifier vos coordonnées, rendez-vous sur 'Mes information' dans la barre de navigation.");
        return "message";
        }
    }


    /**
     * Displays the form for updating participant information.
     *
     * @param model The model used to add the participant object to be displayed in the form.
     * @param username The username of the participant whose information is being updated.
     * @return The name of the view to show the participant information form if the participant exists, or to redirect to the participant information registration form if the participant does not exist.
     */
    @GetMapping("/membre/FicheInfoParticipant/{username}")
    public String displayUpdateParticipantForm(Model model, @PathVariable String username){
        this.restTemplate = new RestTemplate();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        String url="http://localhost:8085/rest/participants/{username}";
        ResponseEntity<Participant> response = restTemplate.getForEntity(url, Participant.class, username);
        Participant participant = response.getBody();
        if (participant != null) {
            model.addAttribute("participant", participant);
            return "membres/ficheInfoParticipant";
        }else{
            return "redirect:/membre/InfoParticipant/{username}";
        }
    }


    /**
     * Updates participant information by processing the participant information modification form submission.
     *
     * @param participant The participant object with the updated information obtained from the form.
     * @param username The username of the participant whose information is being updated.
     * @param model The model used to add attributes for the view.
     * @return The name of the view to display a success message after updating the participant information.
     */
    @PostMapping("/membre/ModificationInfoParticipant/{username}")
    public String updateParticipantInfo (@ModelAttribute("participant") @Valid Participant participant,BindingResult bindingResult, @PathVariable String username, Model model) {
        if (bindingResult.hasErrors()) {
            return "membres/ficheInfoParticipant";
        } else {
        this.restTemplate = new RestTemplate();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        String url = "http://localhost:8085/rest/participants/{id}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Participant> request = new HttpEntity<>(participant, headers);
        ResponseEntity<Participant> response = restTemplate.exchange(url, HttpMethod.POST, request, Participant.class, username);

        model.addAttribute("Message", "La mise à jour de vos coordonnées a bien été enregistrée.");
        return "message";
        }
    }


}
