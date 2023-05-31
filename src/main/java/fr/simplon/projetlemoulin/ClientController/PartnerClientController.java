package fr.simplon.projetlemoulin.ClientController;

import fr.simplon.projetlemoulin.Entities.Partner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class PartnerClientController {

    private RestTemplate restTemplate;

    @GetMapping("/territoire")
    public String displayTerritoirePage(Model model){
        this.restTemplate = new RestTemplate();
        String url ="http://localhost:8085/rest/partners";
        ResponseEntity<List<Partner>> response=restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Partner>>(){});

        List<Partner> partners = response.getBody();
        model.addAttribute("partners", partners);
        return "territoire";
    }


    @GetMapping("/api/partners")
    @ResponseBody
    public List<Partner> getAllPartnersAPIGeolocalisation() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<List<Partner>> response = restTemplate.exchange("http://localhost:8085/rest/api/partners", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Partner>>() {});
        return response.getBody();
    }

}
