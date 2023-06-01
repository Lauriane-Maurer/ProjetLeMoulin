package fr.simplon.projetlemoulin.RestController;

import fr.simplon.projetlemoulin.Entities.ParticipantEvent;
import fr.simplon.projetlemoulin.Repository.ParticipantEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class ParticipantEventRestController {

    @Autowired
    private ParticipantEventRepository repo;

    @Autowired
    public ParticipantEventRestController(ParticipantEventRepository fr) {
        this.repo = fr;
    }

    @GetMapping(path = "/rest/participantEvent")
    public List<ParticipantEvent> getParticipantEvent() {
        return repo.findAll();
    }


    @GetMapping(path = "/rest/participantEventId/{id}")
    public ParticipantEvent getParticipantEventDetails(@PathVariable Long id)throws NoSuchElementException {
        return repo.findById(id).orElseThrow();
    }

    @PostMapping(path="/rest/participantEvent")
    public ParticipantEvent addParticipantEvent(@RequestBody ParticipantEvent newParticipantEvent) {
        return repo.save(newParticipantEvent);
    }

    @GetMapping(path= "/rest/participantEvenement/{participantId}")
    public List<ParticipantEvent> getEventsByParticipantId(@PathVariable Long participantId) {
        return repo.findByParticipantId(participantId);
    }

    @GetMapping(path= "/rest/InscritsEvenement/{evenementId}")
    public List<ParticipantEvent> getParticipantsByEventId(@PathVariable Long eventId) {
        return repo.findByEventId(eventId);
    }

    @GetMapping("/rest/participantEvent/checkRegistration")
    public boolean checkParticipantRegistration(@RequestParam Long participantId, @RequestParam Long eventId) {
        return repo.existsByParticipantIdAndEventId(participantId, eventId);
    }

}
