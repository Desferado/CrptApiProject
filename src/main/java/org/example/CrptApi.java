package org.example;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;

public class CrptApi {
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private final String apiUrl;
    private final RequestThrottler throttler;
    public CrptApi(String apiUrl, int requestLimit, TimeUnit timeUnit){
        this.apiUrl = apiUrl;
        this.throttler = new RequestThrottler(requestLimit, timeUnit);
    }
    public synchronized void createDocument(Document document, String signature){
        throttler.acquirePermission();
        try{
            HttpRequest request = buildRequest(document, signature);
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(this::processResponseBody)
                    .exceptionally(e -> {
                        System.out.println("Ошибка: " + e.getMessage());
                        return null;
                    });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Document convertJsonToDocument (String json){
        Gson gson = new Gson();
        return gson.fromJson(json,Document.class);
    }
    public static String readJsonFromFile(String filePath) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private HttpRequest buildRequest(Document document, String signature){
        String documentToJson = new Gson().toJson(document);
        return HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(documentToJson))
                .build();
    }
    private void processResponseBody(String responseBody){
        // Обработка ответа
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Description {
        private String participant_inn;
    }

@AllArgsConstructor
@Setter
@Getter
    public static class Document {
        private Description description;
        private String doc_id;
        private String doc_status;
        private String doc_type;
        private boolean importRequest;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private String production_date;
        private String production_type;
        private List<Product> products;
        private String reg_date;
        private String reg_number;


    }
    @AllArgsConstructor
    @Setter
    @Getter

    public static class Product {
        private String certificate_document;
        private String certificate_document_date;
        private String certificate_document_number;
        private String owner_inn;
        private String producer_inn;
        private String production_date;
        private String tnved_code;
        private String uit_code;
        private String uitu_code;
    }


    static class RequestThrottler {
        private final Semaphore semaphore;
        public RequestThrottler (int requestLimit, TimeUnit timeUnit){
            semaphore = new Semaphore(requestLimit);
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                    semaphore::release, 0, 1, timeUnit);
        }
        public void acquirePermission(){
            try {
                semaphore.acquire();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
}
