package fr.simplon.projetlemoulin.restcontroller;

import fr.simplon.projetlemoulin.entities.Participant;
import fr.simplon.projetlemoulin.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class ParticipantRestController {

    @Autowired
    private ParticipantRepository repo;


    @GetMapping(path = "/rest/participants")
    public List<Participant> getAllParticipants() {

        return repo.findAll();
    }


    @GetMapping(path = "/rest/participantId/{id}")
    public Participant getparticipantsDetails(@PathVariable Long id)throws NoSuchElementException {
        return repo.findById(id).orElseThrow();
    }


    @PostMapping(path="/rest/participants")
    public Participant addParticipant(@RequestBody Participant newParticipant) {

        return repo.save(newParticipant);
    }

    @PostMapping("/rest/participants/{username}")
    public Participant updateParticipant(@RequestBody Participant newParticipant, @PathVariable String username) {
        Participant participant = repo.findByUsername(username);
        participant.setFirstname(newParticipant.getFirstname());
        participant.setLastname(newParticipant.getLastname());
        participant.setPhone(newParticipant.getPhone());
        participant.setEmail(newParticipant.getEmail());
        participant.setZip_code(newParticipant.getZip_code());
        return repo.save(participant);
    }


    @DeleteMapping("/rest/participants/{id}")
    public void deleteParticipant(@PathVariable Long id) {
        repo.deleteById(id);
    }


    @GetMapping(path = "/rest/participants/{username}")
    public Participant displayParticipantByUsername(@PathVariable String username) {
        return repo.findByUsername(username);
    }
}
