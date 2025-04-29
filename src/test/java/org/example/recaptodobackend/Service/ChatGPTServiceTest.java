package org.example.recaptodobackend.Service;

import org.example.recaptodobackend.Models.Dtos.TodoDto;
import org.example.recaptodobackend.Records.ChatGPTRequest;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestClient;

import org.springframework.web.client.RestClient.RequestBodySpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
class ChatGPTServiceTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MockRestServiceServer mockRestServiceServer;

    @Test
    void checkText_ShouldReturnTrueAndOrignalDescription() {
        // Given
        RestClient.Builder restClientBuilder = mock(RestClient.Builder.class);
        RestClient restClient = mock(RestClient.class);
        RequestBodySpec requestBodySpec = mock(RequestBodySpec.class);
        ResponseSpec responseSpec = mock(ResponseSpec.class);

        when(restClientBuilder.baseUrl(anyString())).thenReturn(restClientBuilder);
        when(restClientBuilder.defaultHeader(anyString(), anyString())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);

        ChatGPTService chatGPTService = new ChatGPTService(restClientBuilder, "test-api-key", "https://mock.api");

        String inputText = "Ein Beispielsatz ohne Fehler.";

        // Erwartete Antwort von der OpenAI API
        String expect = "{\"check\":true, \"text\": \"Ein Beispielsatz ohne Fehler.\"}";

        // Definieren der Mock-Antwort
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo("https://mock.api"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withSuccess(inputText, MediaType.APPLICATION_JSON));

        // Die Methode `checkText` aufrufen
        String actual = chatGPTService.checkText(inputText);

        // Überprüfen, dass der Rückgabewert korrekt ist
        assertEquals("Ein Beispielsatz ohne Fehler.", actual);

        // Überprüfen, dass keine weiteren Anfragen gemacht wurden
        mockRestServiceServer.verify();
    }
}