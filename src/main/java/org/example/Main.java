package org.example;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        String apiUrl = "https://ismp.crpt.ru/api/v3/lk/documents/create";
        CrptApi crptApi = new CrptApi(apiUrl, 1, TimeUnit.SECONDS);
        String json = CrptApi.readJsonFromFile("src/main/resources/document.json");
        CrptApi.Document document = CrptApi.convertJsonToDocument(json);
        String signature = "signature";
        crptApi.createDocument(document, signature);
    }
}