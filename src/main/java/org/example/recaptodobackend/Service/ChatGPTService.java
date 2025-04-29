package org.example.recaptodobackend.Service;

import org.example.recaptodobackend.Records.ChatGPTRequest;
import org.example.recaptodobackend.Records.ChatGPTResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ChatGPTService {

    private final RestClient restClient;

    public ChatGPTService(RestClient.Builder restClient,
    @Value("$app.openai-api-key") String openaiApiKey,
    @Value("$api.base-url") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .build();
    }
    public String checkText(String text) {
        String msg = "Hallo, kannst Du mir bitte nur als JSON " +
                "(z.B. {check:true/false, text: corrected text}) " +
                "ausgeben, ob folgende Beschreibung ohne Rechtschreibfehler ist? Text: "+text;

        ChatGPTResponse response = restClient.post()
                .body(new ChatGPTRequest(msg))
                .retrieve()
                .body(ChatGPTResponse.class);
        return response.text();

    }
}
