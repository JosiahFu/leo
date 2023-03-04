import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OpenAIController {
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String OPENAI_API_KEY = "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    @PostMapping("/generateCompletion")
    public String generateCompletion(@RequestBody String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        HttpEntity<String> entity = new HttpEntity<>("{\n" +
            "  \"model\": \"gpt-3.5-turbo\",\n" +
            "  \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt.getContent() + "\"}]\n" +
            "}", headers);

        ResponseEntity<CompletionResponse> response = restTemplate.exchange(
            "https://api.openai.com/v1/chat/completions",
            HttpMethod.POST,
            entity,
            Completion.class);
        
        return response.getBody().getChoices().get(0).getMessage().getContent();;
    }
}

private static class CompletionResponse {
    private String id;
    private String object;
    private long created;
    private Choice[] choices;
    private Usage usage;

    private static class Choices
}