package org.example;

import com.google.gson.Gson;
import org.example.model.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args)  {
        // Создаем экземпляр класса CrptApi
        CrptApi api = new CrptApi(TimeUnit.SECONDS,1,"https://ismp.crpt.ru/api/v3/lk/documents/create");

        // Читаем JSON из файла
        String json = "";
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/document.json"));
            json = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Преобразуем JSON в объект Document
        Gson gson = new Gson();
        Document document = gson.fromJson(json, Document.class);

        // Определяем подпись
        String signature = "signature";

        // Вызываем метод создания документа createDocument()
        api.createDocument(document, signature);

        // Завершаем работу с api
        api.shutdown();
    }
}