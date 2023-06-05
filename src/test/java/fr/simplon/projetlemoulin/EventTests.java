package fr.simplon.projetlemoulin;

import fr.simplon.projetlemoulin.entities.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EventTests {


    /***************************** Tests on Event CRUD ********************************/


    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private Event event;

    private final String URL = "http://localhost:8085/rest/events";

    @BeforeEach
    public void init()
    {
        this.restTemplate = new RestTemplate();

        this.event = new Event();
        this.event.setType("Test Event Type");
        this.event.setTitle("Test Event Title");
        this.event.setDescription("Test Event Description");
        this.event.setStart_date(LocalDateTime.of(2023, 8, 9, 10, 0));
        this.event.setEnd_date(LocalDateTime.of(2023, 8, 9, 17, 0));
        this.event.setLimit_places(true);
        this.event.setTotal_places(10);
        this.event.setAvailable_places(10);
        this.event.setPrice(5.0);
        this.event.setSpeaker("Test Event Speaker");
        this.event.setParticipantEvents(null);
        this.event.setPartners(null);
    }


    /**
     * Tests the addition of a new event.
     */
    @Test
    public void testAddSurvey() {
        ResponseEntity<Event> response = restTemplate.postForEntity(URL, event, Event.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals(event.getType(), response.getBody().getType());
        assertEquals(event.getTitle(), response.getBody().getTitle());
        assertEquals(event.getDescription(), response.getBody().getDescription());
        assertEquals(event.getStart_date(), response.getBody().getStart_date());
        assertEquals(event.getEnd_date(), response.getBody().getEnd_date());
        assertEquals(event.getTotal_places(), response.getBody().getTotal_places());
        assertEquals(event.getAvailable_places(), response.getBody().getAvailable_places());
        assertEquals(event.getPrice(), response.getBody().getPrice());
        assertEquals(event.getSpeaker(), response.getBody().getSpeaker());
        assertEquals(event.getParticipantEvents(), response.getBody().getParticipantEvents());
        assertEquals(event.getPartners(), response.getBody().getPartners());
    }


    /**
     * Tests the display of all Events.
     */
    @Test
    public void testDisplayAllEvents() {
        ResponseEntity<List<Event>> response = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Event>>() {
                }
        );
        List<Event> events = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(events);
        Assertions.assertFalse(events.isEmpty());
    }


    /**
     * Tests the display of a specific event.
     */
    @Test
    public void testDisplayEventById() {
        ResponseEntity<Event> response1 = restTemplate.postForEntity(URL, event, Event.class);

        // Retrieval of the ID of the created event
        Long newEventId = response1.getBody().getId();
        Assertions.assertNotNull(newEventId);

        ResponseEntity<Event> response2 = restTemplate.exchange(
                URL + "/" + newEventId,
                HttpMethod.GET,
                null,
                Event.class,
                newEventId
        );

        // Checking of retrieved event data
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        Event retrievedEvent = response2.getBody();
        assertNotNull(retrievedEvent);
        assertEquals(newEventId, retrievedEvent.getId());
        assertEquals(event.getType(), retrievedEvent.getType());
        assertEquals(event.getTitle(), retrievedEvent.getTitle());
        assertEquals(event.getDescription(), retrievedEvent.getDescription());
        assertEquals(event.getStart_date(), retrievedEvent.getStart_date());
        assertEquals(event.getEnd_date(), retrievedEvent.getEnd_date());
        assertEquals(event.getTotal_places(), retrievedEvent.getTotal_places());
        assertEquals(event.getAvailable_places(), retrievedEvent.getAvailable_places());
        assertEquals(event.getPrice(), retrievedEvent.getPrice());
        assertEquals(event.getSpeaker(), retrievedEvent.getSpeaker());
        assertEquals(event.getParticipantEvents(), retrievedEvent.getParticipantEvents());
        assertEquals(event.getPartners(), retrievedEvent.getPartners());
    }



    /**
     This method tests the update of an event.
     It creates an event and then modifies the data of the event.
     The ID of the created event is retrieved to modify it.
     The test then verifies if the update was successful by comparing the updated event data with the retrieved data.
     */
    @Test
    public void testUpdateEvent() {

        ResponseEntity<Event> response1 = restTemplate.postForEntity(URL, event, Event.class);

        // Récupération de l'identifiant du sondage créé
        Long newEventId = response1.getBody().getId();
        Assertions.assertNotNull(newEventId);

        // Mise à jour (nouvelle URL = url + "/" + newEventId)
        Event event = restTemplate.getForObject(URL + "/" + newEventId, Event.class, newEventId);
        event.setTitle("Test Update Event");
        event.setDescription("Test Update réussi");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Event> request = new HttpEntity<>(event, headers);

        ResponseEntity<Event> response2 = restTemplate.exchange(
                "http://localhost:8085/rest/UpdateEvent/{id}",
                HttpMethod.POST,
                request,
                Event.class,
                newEventId);

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(newEventId, response2.getBody().getId());
        assertEquals(event.getTitle(), response2.getBody().getTitle());
        assertEquals(event.getDescription(), response2.getBody().getDescription());

    }


    /**
     This method tests the deletion of an event.
     It creates an event and then deletes it.
     The test then verifies if the deletion was successful by checking the HTTP status code of the response.
     */
    @Test
    public void testDeleteEvent() {

        ResponseEntity<Event> response1 = restTemplate.postForEntity(URL, event, Event.class);

        // Récupération de l'identifiant du sondage créé
        Long newEventId = response1.getBody().getId();
        Assertions.assertNotNull(newEventId);

        // Suppression du sondage créé
        HttpEntity<Void> requestDelete = new HttpEntity<>(headers);
        ResponseEntity<Void> responseDelete = restTemplate.exchange(
                URL + "/" + newEventId,
                HttpMethod.DELETE,
                requestDelete,
                Void.class,
                newEventId);

        //Verification du code de statut HTTP de la réponse de la requête HTTP. Il doit être OK si la suppression a bien été effectuée.
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
    }

}
