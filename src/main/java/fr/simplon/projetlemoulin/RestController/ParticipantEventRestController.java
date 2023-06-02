package fr.simplon.projetlemoulin.RestController;

import fr.simplon.projetlemoulin.Entities.Event;
import fr.simplon.projetlemoulin.Entities.ParticipantEvent;
import fr.simplon.projetlemoulin.Repository.EventRepository;
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
    private EventRepository eventRepo;

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

    @GetMapping(path= "/rest/participantEvent/{participantId}")
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



    @DeleteMapping("/rest/participantEvent/{id}")
    public void deleteParticipantEvent(@PathVariable Long id) {


        ParticipantEvent participantEvent = repo.findById(id).orElse(null);
        if (participantEvent != null) {
            Event event = participantEvent.getEvent();

            // Incrémentez le nombre de places restantes de l'événement
            if (event != null) {
                event.incrementAvailablePlaces();
                eventRepo.save(event);
            }
        }
        repo.deleteById(id);
    }

}
