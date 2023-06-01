package fr.simplon.projetlemoulin.RestController;

import fr.simplon.projetlemoulin.Entities.Event;
import fr.simplon.projetlemoulin.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * RestController class for managing events.
 *
 * This class serves as the REST API controller for handling event-related operations.
 * It provides endpoints for retrieving, adding, updating, and deleting events (CRUD).
 *
 * @RestController indicates that this class is a controller that handles RESTful requests
 */

@RestController
public class EventRestController {

    @Autowired
    private EventRepository repo;


    @Autowired
    public EventRestController(EventRepository fr) {
        this.repo = fr;

        //this.repo.save(new Event("Stage", "Fresque de la biodiversité","Fresque de la biodiversité, kesako? Un atelier collaboratif pour mieux comprendre le rôle des écosystèmes et imaginer ensemble comment inverser les tendances de leurs effondrement. Une expérience d’intelligence collective qui parle autant à la tête qu’au coeur.", LocalDateTime.of(2023, 8, 9, 10,00), LocalDateTime.of(2023, 8, 9, 17, 00),true, 10, 10, 5.00, "Nathalie Richard", "https://picsum.photos/id/290/150.webp",null));
    }

    /**
     * Retrieves all events from the repository.
     *
     * @return A list of all events.
     */
    @GetMapping(path = "/rest/events")
    public List<Event> getEvent() {
        return repo.findAll();
    }


    /**
     * Retrieves all events as JSON from the repository.
     *
     * @return A list of all events in JSON format.
     */
    @GetMapping("/rest/eventsCards")
    @ResponseBody
    public List<Event> getAllEventsAsJson() {
        List<Event> events = repo.findAll();
        return events;
    }


    /**
     * Retrieves details of a specific event by its ID.
     *
     * @param id The ID of the event to retrieve.
     * @return The event with the specified ID.
     * @throws NoSuchElementException if the event with the given ID is not found.
     */
    @GetMapping(path = "/rest/events/{id}")
    public Event getEventDetails(@PathVariable Long id)throws NoSuchElementException {
        return repo.findById(id).orElseThrow();
    }


    /**
     * Adds a new event to the repository.
     *
     * @param newEvent The event to add.
     * @return The newly added event.
     */
    @PostMapping(path="/rest/events")
    public Event addEvent(@RequestBody Event newEvent) {
        return repo.save(newEvent);
    }

    /**
     * Updates an existing event in the repository.
     *
     * @param newEvent The updated event data.
     * @param id The ID of the event to update.
     * @return The updated event.
     */
    @PostMapping("/rest/UpdateEvent/{id}")
    public Event updateEvent(@RequestBody Event newEvent, @PathVariable Long id) {
        return repo.findById(id)
                .map(event -> {
                    event.setType(newEvent.getType());
                    event.setTitle(newEvent.getTitle());
                    event.setDescription(newEvent.getDescription());
                    event.setStart_date(newEvent.getStart_date());
                    event.setEnd_date(newEvent.getEnd_date());
                    event.setLimit_places(newEvent.isLimit_places());
                    event.setTotal_places(newEvent.getTotal_places());
                    event.setAvailable_places(newEvent.getAvailable_places());
                    event.setPrice(newEvent.getPrice());
                    event.setSpeaker(newEvent.getSpeaker());
                    event.setPhoto(newEvent.getPhoto());
                    event.setParticipantEvents(newEvent.getParticipantEvents());
                    event.setPartners(newEvent.getPartners());
                    return repo.save(event);
                })
                .orElseGet(() -> {
                    newEvent.setId(id);
                    return repo.save(newEvent);
                });
    }


    /**
     * Deletes an event from the repository.
     *
     * This method handles the DELETE request for the "/rest/events/{id}" endpoint and
     * deletes the event with the specified ID from the repository.
     *
     * @param id The ID of the event to delete.
     */
    @DeleteMapping("/rest/events/{id}")
    public void deleteEvent(@PathVariable Long id) {
        repo.deleteById(id);
    }



    @PostMapping("/rest/DecrementAvailablePlaces/{id}")
    public Event decremenetAvailablePlaces(@RequestBody Event newEvent, @PathVariable Long id) {
        return repo.findById(id)
                .map(event -> {
                    event.decrementAvailablePlaces();
                    return repo.save(event);
                })
                .orElseGet(() -> {
                    newEvent.setId(id);
                    return repo.save(newEvent);
                });
    }
}

