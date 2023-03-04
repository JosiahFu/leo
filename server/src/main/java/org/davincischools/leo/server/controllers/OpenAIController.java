package org.davincischools.leo.server.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");

    @PostMapping(value = "/generateCompletion", consumes = "text/plain", produces = "application/json")
    public String generateCompletion(@RequestBody String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("Content-Type", "application/json");

        String requestBody = "{\n  \"model\": \"gpt-3.5-turbo\",\n  \"messages\": [{\"role\": \"system\", \"content\": \"You are a creative and helpful assistant that specializes in creating high school project ideas\"}, {\"role\": \"user\", \"content\": \"" + prompt + "\"}]\n}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
            OPENAI_API_URL,
            HttpMethod.POST,
            entity,
            String.class
        );

        return response.getBody();
    }
}