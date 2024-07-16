package fr.rp.springapiparc.rest;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ApikeyService {

    private final RestTemplate restTemplate;

    public ApikeyService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public boolean validateApiKey(String apiKey) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", apiKey);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8083/apikey/key", HttpMethod.GET, entity, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "apikey non valide");
        }
    }


}
