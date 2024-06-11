package org.example;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.*;

import static org.example.ForTests.document_1;
import static org.example.ForTests.json;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CrptApiTest {

    @InjectMocks
    private HttpClient mockHttpClient;
    @Mock
    private CrptApi crptApi;
    @Mock
    private HttpResponse mockResponse;


    @BeforeEach
    void setUp() {
        mockHttpClient = mock(HttpClient.class);
        crptApi = new CrptApi(TimeUnit.SECONDS, 1, "http://api.example.com");
        mockResponse = Mockito.mock(HttpResponse.class);
    }

    @Test
    void testCreateDocument() {
        String signature = "signature";
        CompletableFuture<HttpResponse> future = CompletableFuture.completedFuture(mockResponse);

        when(mockHttpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(future);
        when(mockResponse.body()).thenReturn(json);
        when(mockResponse.statusCode()).thenReturn(200);

        CompletableFuture<Void> result = crptApi.createDocument(document_1, signature);

        // Проверяем, что запрос был отправлен
        verify(mockHttpClient).sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        // Проверяем, что тело ответа было обработано
        result.thenRun(() -> verify(mockResponse).body());
        // Проверяем, что статус код был проверен
        result.thenRun(() -> assertEquals(200, mockResponse.statusCode()));
    }

    @AfterEach
    void tearDown() {
        crptApi.shutdown();
    }
}
