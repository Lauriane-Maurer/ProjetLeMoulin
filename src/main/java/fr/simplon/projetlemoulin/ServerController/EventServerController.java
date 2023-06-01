package fr.simplon.projetlemoulin.ServerController;

import fr.simplon.projetlemoulin.Entities.Event;
import fr.simplon.projetlemoulin.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class EventServerController {

    @Autowired
    private EventRepository repo;


    @Autowired
    public EventServerController(EventRepository fr) {
        this.repo = fr;

        //this.repo.save(new Event("Stage", "Fresque de la biodiversité","Fresque de la biodiversité, kesako? Un atelier collaboratif pour mieux comprendre le rôle des écosystèmes et imaginer ensemble comment inverser les tendances de leurs effondrement. Une expérience d’intelligence collective qui parle autant à la tête qu’au coeur.", LocalDateTime.of(2023, 8, 9, 10,00), LocalDateTime.of(2023, 8, 9, 17, 00),true, 10, 10, 5.00, "Nathalie Richard", "https://picsum.photos/id/290/150.webp",null));
    }


    @GetMapping(path = "/rest/events")
    public List<Event> getEvent() {
        return repo.findAll();
    }


    @GetMapping("/rest/api/events")
    @ResponseBody
    public List<Event> getAllEventsAsJson() {
        List<Event> events = repo.findAll();
        return events;
    }


    @GetMapping(path = "/rest/events/{id}")
    public Event getEventDetails(@PathVariable Long id)throws NoSuchElementException {
        return repo.findById(id).orElseThrow();
    }


    @PostMapping(path="/rest/events")
    public Event addEvent(@RequestBody Event newEvent) {
        return repo.save(newEvent);
    }


    @PostMapping("/rest/UpdateEvent/{id}")
    public Event updateEvent(@RequestBody Event newEvent, @PathVariable Long id) {
        return repo.findById(id)
                .map(event -> {
                    event.setType(newEvent.getType());
                    event.setTitle(newEvent.getTitle());
                    event.setDescription(newEvent.getDescription());
                    event.setStartDate(newEvent.getStartDate());
                    event.setEndDate(newEvent.getEndDate());
                    event.setLimit_places(newEvent.isLimit_places());
                    event.setTotal_places(newEvent.getTotal_places());
                    event.setAvailable_places(newEvent.getAvailable_places());
                    event.setPrice(newEvent.getPrice());
                    event.setSpeaker(newEvent.getSpeaker());
                    event.setPhoto(newEvent.getPhoto());
                    event.setParticipantEvents(newEvent.getParticipantEvents());
                    return repo.save(event);
                })
                .orElseGet(() -> {
                    newEvent.setId(id);
                    return repo.save(newEvent);
                });
    }

    @DeleteMapping("/rest/events/{id}")
    public void deleteEvent(@PathVariable Long id) {
        repo.deleteById(id);
    }


}

