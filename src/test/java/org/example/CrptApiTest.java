package org.example;
import static org.example.ForTests.document_1;
import static org.example.ForTests.json;
import static org.junit.Assert.*;
import org.junit.*;
import org.example.CrptApi.*;
import java.util.concurrent.TimeUnit;
import static org.mockito.Mockito.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;


public class CrptApiTest {
    private CrptApi crptApi;
    private CrptApi.Document testDocument;
    private String testSignature;
    private String testApiUrl = "http://api.example.com";
    private int testRequestLimit = 10;
    private TimeUnit testTimeUnit = TimeUnit.SECONDS;
    private HttpClient mockHttpClient;
    private RequestThrottler mockThrottler;
    @Before
    public void setUp() {
        crptApi = new CrptApi(testApiUrl, testRequestLimit, testTimeUnit);
        mockHttpClient = mock(HttpClient.class);
        mockThrottler = mock(RequestThrottler.class);
        crptApi = new CrptApi("http://api.example.com", 10, TimeUnit.SECONDS);
        testSignature = "testSignature";
    }

    @Test
    public void testCreateDocument() throws Exception {
        // Подготовка мокированного ответа
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn("responseBody");

        // Мокирование sendAsync для возвращения CompletableFuture с мокированным ответом
        when(mockHttpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));

        // Вызов тестируемого метода
        crptApi.createDocument(document_1, testSignature);

        // Проверка, что acquirePermission был вызван
        verify(mockThrottler).acquirePermission();

        // Проверка, что sendAsync был вызван
        verify(mockHttpClient).sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testConvertJsonToDocument() {
        // Вызов метода для преобразования JSON в объект Document
        CrptApi.Document resultDocument = CrptApi.convertJsonToDocument(json);

        // Проверки, что поля объекта Document соответствуют данным из JSON
        assertNotNull("Результат не должен быть null", resultDocument);
        assertEquals("Неверный participant_inn в description", "1234567890", resultDocument.getDescription().getParticipant_inn());
        assertEquals("Неверный doc_id", "doc123", resultDocument.getDoc_id());
        assertEquals("Неверный doc_status", "active", resultDocument.getDoc_status());
        assertEquals("Неверный doc_type", "type1", resultDocument.getDoc_type());
        assertFalse("Неверное значение importRequest", resultDocument.isImportRequest());
        assertEquals("Неверный owner_inn", "0987654321", resultDocument.getOwner_inn());
        assertEquals("Неверный participant_inn", "1234567890", resultDocument.getParticipant_inn());
        assertEquals("Неверный producer_inn", "1112131415", resultDocument.getProducer_inn());
        assertEquals("Неверная production_date", "2024-06-10", resultDocument.getProduction_date());
        assertEquals("Неверный production_type", "typeA", resultDocument.getProduction_type());
        assertTrue("Список products должен быть пустым", resultDocument.getProducts().isEmpty());
        assertEquals("Неверная reg_date", "2024-06-10", resultDocument.getReg_date());
        assertEquals("Неверный reg_number", "RN123", resultDocument.getReg_number());
    }
}