package fr.simplon.projetlemoulin;

import fr.simplon.projetlemoulin.entities.Event;
import fr.simplon.projetlemoulin.entities.Participant;
import fr.simplon.projetlemoulin.repositories.ParticipantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ParticipantTests {

    @Autowired
    private ParticipantRepository repo;
    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private Participant participant;

    private final String URL = "http://localhost:8085/rest/participants";

    @BeforeEach
    public void init()
    {
        this.restTemplate = new RestTemplate();

        this.participant = new Participant();
        this.participant.setFirstname("Test Participant Firstname");
        this.participant.setLastname("Test Participant Lastname");
        this.participant.setPhone("Test Participant Phone");
        this.participant.setEmail("Test Participant Email");
        this.participant.setZip_code("29200");
        this.participant.setUsername("TestUsername");
        this.participant.setParticipantEvents(null);
    }

    /**
     * Tests the addition of a new participant.
     */
    @Test
    public void testAddParticipant() {
        ResponseEntity<Participant> response = restTemplate.postForEntity(URL, participant, Participant.class);
        Long newParticipantId = response.getBody().getId();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals(participant.getFirstname(), response.getBody().getFirstname());
        assertEquals(participant.getLastname(), response.getBody().getLastname());
        assertEquals(participant.getPhone(), response.getBody().getPhone());
        assertEquals(participant.getEmail(), response.getBody().getEmail());
        assertEquals(participant.getZip_code(), response.getBody().getZip_code());
        assertEquals(participant.getUsername(), response.getBody().getUsername());
        assertEquals(participant.getParticipantEvents(), response.getBody().getParticipantEvents());
        repo.deleteById(newParticipantId);
    }

    /**
     * Tests the display of all participants.
     */
    @Test
    public void testDisplayAllParticipants() {
        ResponseEntity<Participant> response1 = restTemplate.postForEntity(URL, participant, Participant.class);
        Long newParticipantId = response1.getBody().getId();

        ResponseEntity<List<Participant>> response = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Participant>>() {
                }
        );
        List<Participant> participants = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(participants);
        Assertions.assertFalse(participants.isEmpty());

        repo.deleteById(newParticipantId);
    }


    /**
     * Tests the display of a specific participant.
     */
    @Test
    public void testDisplayParticipantById() {
        ResponseEntity<Participant> response1 = restTemplate.postForEntity(URL, participant, Participant.class);

        // Retrieval of the ID of the created participant
        Long newParticipantId = response1.getBody().getId();
        Assertions.assertNotNull(newParticipantId);

        ResponseEntity<Participant> response2 = restTemplate.exchange(
                "http://localhost:8085/rest/participantId/"+ newParticipantId,
                HttpMethod.GET,
                null,
                Participant.class,
                newParticipantId);

        // Checking of retrieved participant data
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        Participant retrievedParticipant = response2.getBody();
        assertNotNull(retrievedParticipant);
        assertEquals(newParticipantId, retrievedParticipant.getId());
        assertEquals(participant.getFirstname(), retrievedParticipant.getFirstname());
        assertEquals(participant.getLastname(), retrievedParticipant.getLastname());
        assertEquals(participant.getPhone(), retrievedParticipant.getPhone());
        assertEquals(participant.getEmail(), retrievedParticipant.getEmail());
        assertEquals(participant.getZip_code(), retrievedParticipant.getZip_code());
        assertEquals(participant.getUsername(), retrievedParticipant.getUsername());
        assertEquals(participant.getParticipantEvents(), retrievedParticipant.getParticipantEvents());
        repo.deleteById(newParticipantId);
    }



    /**
     This method tests the update of a participant.
     It creates a participant and then modifies the data of the participant.
     The ID of the created participant is retrieved to modify it.
     The test then verifies if the update was successful by comparing the updated participant data with the retrieved data.
     */
    @Test
    public void testUpdateParticipant() {

        ResponseEntity<Participant> response1 = restTemplate.postForEntity(URL, participant, Participant.class);

        // Récupération de l'identifiant du participant créé
        Long newParticipantId = response1.getBody().getId();
        String newParticipantUsername = response1.getBody().getUsername();
        Assertions.assertNotNull(newParticipantId);

        // Mise à jour (nouvelle URL = url + "/" + newParticipantId)
        Participant participant = restTemplate.getForObject("http://localhost:8085/rest/participantId/"+ newParticipantId, Participant.class, newParticipantId);
        participant.setFirstname("Test Update Participant");
        participant.setLastname("Test Update réussi");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Participant> request = new HttpEntity<>(participant, headers);

        ResponseEntity<Participant> response2 = restTemplate.exchange(
                "http://localhost:8085/rest/participants/{username}",
                HttpMethod.POST,
                request,
                Participant.class,
                newParticipantUsername);

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(newParticipantId, response2.getBody().getId());
        assertEquals(participant.getFirstname(), response2.getBody().getFirstname());
        assertEquals(participant.getLastname(), response2.getBody().getLastname());

        repo.deleteById(newParticipantId);
    }


    /**
     This method tests the deletion of a participant.
     It creates a participant and then deletes it.
     The test then verifies if the deletion was successful by checking the HTTP status code of the response.
     */
    @Test
    public void testDeleteParticipant() {

        ResponseEntity<Participant> response1 = restTemplate.postForEntity(URL, participant, Participant.class);

        // Récupération de l'identifiant du participant créé
        Long newParticipantId = response1.getBody().getId();
        Assertions.assertNotNull(newParticipantId);

        // Suppression du participant créé
        HttpEntity<Void> requestDelete = new HttpEntity<>(headers);
        ResponseEntity<Void> responseDelete = restTemplate.exchange(
                URL + "/" + newParticipantId,
                HttpMethod.DELETE,
                requestDelete,
                Void.class,
                newParticipantId);

        //Verification du code de statut HTTP de la réponse de la requête HTTP. Il doit être OK si la suppression a bien été effectuée.
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
    }
}
