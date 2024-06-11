package org.example;
import org.example.CrptApi.*;

public class ForTests {
    public static Description description_1 = new Description("description_1");
    public static Document document_1 = new Document (
    description_1,
    "doc123",
    "active",
    "type1",
            false,
    "0987654321",
    "1234567890",
    "1112131415",
    "2024-06-10",
    "typeA",
    null,
    "2024-06-10",
    "RN123"
            );
    public static String json = "{\"description\":{\"participant_inn\":\"1234567890\"},"
            + "\"doc_id\":\"doc123\","
            + "\"doc_status\":\"active\","
            + "\"doc_type\":\"type1\","
            + "\"importRequest\":false,"
            + "\"owner_inn\":\"0987654321\","
            + "\"participant_inn\":\"1234567890\","
            + "\"producer_inn\":\"1112131415\","
            + "\"production_date\":\"2024-06-10\","
            + "\"production_type\":\"typeA\","
            + "\"products\":[],"
            + "\"reg_date\":\"2024-06-10\","
            + "\"reg_number\":\"RN123\"}";
}
