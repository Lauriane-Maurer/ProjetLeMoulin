package fr.simplon.projetlemoulin;

import fr.simplon.projetlemoulin.entities.Participant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ProjetLeMoulinApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    public void testAddParticipant() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        Participant participant = new Participant("test", "test", "test", "test", "test", "test", null);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Participant> request = new HttpEntity<>(participant, headers);

        ResponseEntity<Participant> response = restTemplate.exchange("http://localhost:8080/rest/participants",
                HttpMethod.POST,
                request,
                Participant.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals(participant.getFirstname(), response.getBody().getFirstname());
        assertEquals(participant.getLastname(), response.getBody().getLastname());
        assertEquals(participant.getPhone(), response.getBody().getPhone());
        assertEquals(participant.getEmail(), response.getBody().getEmail());
        assertEquals(participant.getZip_code(), response.getBody().getZip_code());
        assertEquals(participant.getUsername(), response.getBody().getUsername());
    }

}
