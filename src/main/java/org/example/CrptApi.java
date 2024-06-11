package org.example;


import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.*;

public class CrptApi {
    private final HttpClient httpClient;
    private final String apiUrl;
    private final Semaphore semaphore;
    private final ScheduledExecutorService scheduler;

    public CrptApi(TimeUnit timeUnit, int requestLimit, String apiUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.apiUrl = apiUrl;
        this.semaphore = new Semaphore(requestLimit);
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(semaphore::release, 0, 1, timeUnit);
    }

    /**
     * Метод для создания документа. Использование CompletableFuture для асинхронных операций:
     * Используюся асинхронные методы HttpClient, чтобы не блокировать основной поток.
     * @param document
     * @param signature
     * Возвращает
     */
    public CompletableFuture<Void> createDocument(Document document, String signature) {
        return CompletableFuture.runAsync(() -> {
            try {
                semaphore.acquire();
                HttpRequest request = buildRequest(document, signature);
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAccept(this::processResponseBody)
                        .exceptionally(e -> {
                            System.out.println("Ошибка: " + e.getMessage());
                            return null;
                        });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * Метод создает и возвращает новый HTTP-запрос с использованием данных документа и подписи.
     * @param document
     * @param signature
     */
    private HttpRequest buildRequest(Document document, String signature) {
        Gson gson = new Gson();
        String json = gson.toJson(document);
        return HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    private void processResponseBody(String responseBody) {
        // Обработка тела ответа
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Description {
        private String participant_inn;
    }

    /**
     * Класс документа
     */
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
}

