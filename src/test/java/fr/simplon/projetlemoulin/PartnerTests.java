package fr.simplon.projetlemoulin;

import fr.simplon.projetlemoulin.entities.Partner;
import fr.simplon.projetlemoulin.repositories.PartnerRepository;
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
public class PartnerTests {

    /***************************** Tests on Partner CRUD ********************************/

    @Autowired
    private PartnerRepository partnerRepo;

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private Partner partner;

    private final String partnerURL = "http://localhost:8085/rest/partners";

    @BeforeEach
    public void initPartnerTests()
    {
        this.restTemplate = new RestTemplate();

        this.partner = new Partner();
        this.partner.setName("Test Partner Name");
        this.partner.setActivity("Test Partner Activity");
        this.partner.setDescription("Test Partner Description");
        this.partner.setAddress("Test Partner Adress");
        this.partner.setTown("Test Partner Town");
        this.partner.setZip_code("29200");
        this.partner.setUrl("https://test.test.test");
        this.partner.setLatitude(48.001);
        this.partner.setLongitude(-4.001);
    }



    /**
     * Tests the addition of a new partner.
     */
    @Test
    public void testAddPartner() {
        ResponseEntity<Partner> response = restTemplate.postForEntity(partnerURL, partner, Partner.class);
        Long newPartnerId = response.getBody().getId();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals(partner.getName(), response.getBody().getName());
        assertEquals(partner.getActivity(), response.getBody().getActivity());
        assertEquals(partner.getDescription(), response.getBody().getDescription());
        assertEquals(partner.getAddress(), response.getBody().getAddress());
        assertEquals(partner.getTown(), response.getBody().getTown());
        assertEquals(partner.getZip_code(), response.getBody().getZip_code());
        assertEquals(partner.getUrl(), response.getBody().getUrl());
        assertEquals(partner.getLatitude(), response.getBody().getLatitude());
        assertEquals(partner.getLongitude(), response.getBody().getLongitude());
        assertEquals(partner.getEvents(), response.getBody().getEvents());
        partnerRepo.deleteById(newPartnerId);
    }


    /**
     * Tests the display of all partners.
     */
    @Test
    public void testDisplayAllPartners() {
        ResponseEntity<Partner> response1 = restTemplate.postForEntity(partnerURL, partner, Partner.class);
        Long newPartnerId = response1.getBody().getId();

        ResponseEntity<List<Partner>> response = restTemplate.exchange(
                partnerURL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Partner>>() {
                }
        );
        List<Partner> partners = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(partners);
        Assertions.assertFalse(partners.isEmpty());

        partnerRepo.deleteById(newPartnerId);
    }


    /**
     * Tests the display of a specific partner.
     */
    @Test
    public void testDisplayPartnerById() {
        ResponseEntity<Partner> response1 = restTemplate.postForEntity(partnerURL, partner, Partner.class);

        // Retrieval of the ID of the created partner
        Long newPartnerId = response1.getBody().getId();
        Assertions.assertNotNull(newPartnerId);

        ResponseEntity<Partner> response2 = restTemplate.exchange(
                partnerURL + "/" + newPartnerId,
                HttpMethod.GET,
                null,
                Partner.class,
                newPartnerId
        );

        // Checking of retrieved partner data
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        Partner retrievedPartner = response2.getBody();
        assertNotNull(retrievedPartner);
        assertEquals(newPartnerId, retrievedPartner.getId());
        assertEquals(partner.getName(), retrievedPartner.getName());
        assertEquals(partner.getActivity(), retrievedPartner.getActivity());
        assertEquals(partner.getDescription(), retrievedPartner.getDescription());
        assertEquals(partner.getAddress(), retrievedPartner.getAddress());
        assertEquals(partner.getTown(), retrievedPartner.getTown());
        assertEquals(partner.getZip_code(), retrievedPartner.getZip_code());
        assertEquals(partner.getUrl(), retrievedPartner.getUrl());
        assertEquals(partner.getLatitude(), retrievedPartner.getLatitude());
        assertEquals(partner.getLongitude(), retrievedPartner.getLongitude());
        assertEquals(partner.getEvents(), retrievedPartner.getEvents());
        partnerRepo.deleteById(newPartnerId);
    }



    /**
     This method tests the update of a partner.
     It creates a partner and then modifies the data of the partner.
     The ID of the created partner is retrieved to modify it.
     The test then verifies if the update was successful by comparing the updated partner data with the retrieved data.
     */
    @Test
    public void testUpdatePartner() {

        ResponseEntity<Partner> response1 = restTemplate.postForEntity(partnerURL, partner, Partner.class);

        // Récupération de l'identifiant du partenaire créé
        Long newPartnerId = response1.getBody().getId();
        Assertions.assertNotNull(newPartnerId);

        // Mise à jour (nouvelle URL = url + "/" + newPartnerId)
        Partner partner = restTemplate.getForObject(partnerURL + "/" + newPartnerId, Partner.class, newPartnerId);
        partner.setName("Test Update Partner");
        partner.setDescription("Test Update réussi");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Partner> request = new HttpEntity<>(partner, headers);

        ResponseEntity<Partner> response2 = restTemplate.exchange(
                "http://localhost:8085/rest/updatePartner/{id}",
                HttpMethod.PUT,
                request,
                Partner.class,
                newPartnerId);

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(newPartnerId, response2.getBody().getId());
        assertEquals(partner.getName(), response2.getBody().getName());
        assertEquals(partner.getDescription(), response2.getBody().getDescription());

        partnerRepo.deleteById(newPartnerId);
    }


    /**
     This method tests the deletion of a partner.
     It creates a partner and then deletes it.
     The test then verifies if the deletion was successful by checking the HTTP status code of the response.
     */
    @Test
    public void testDeletePartner() {

        ResponseEntity<Partner> response1 = restTemplate.postForEntity(partnerURL, partner, Partner.class);

        // Récupération de l'identifiant du partenaire créé
        Long newPartnerId = response1.getBody().getId();
        Assertions.assertNotNull(newPartnerId);

        // Suppression du partenaire créé
        HttpEntity<Void> requestDelete = new HttpEntity<>(headers);
        ResponseEntity<Void> responseDelete = restTemplate.exchange(
                partnerURL + "/" + newPartnerId,
                HttpMethod.DELETE,
                requestDelete,
                Void.class,
                newPartnerId);

        //Verification du code de statut HTTP de la réponse de la requête HTTP. Il doit être OK si la suppression a bien été effectuée.
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
    }

}


