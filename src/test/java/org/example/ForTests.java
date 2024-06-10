package org.example;

public class ForTests {
    static CrptApi.Description description_1 = new CrptApi.Description("description_1");
    public static CrptApi.Document document_1 = new CrptApi.Document (
    description_1,
    "doc_id_1",
    "doc_status_1",
    "doc_type_1",
     true,
    "owner_inn_1",
    "participant_inn_1",
    "producer_inn_1",
    "production_date_1",
    "production_type_1",
    null,
    "reg_date_1",
    "reg_number_1"
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
