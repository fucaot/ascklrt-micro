package com.ascklrt.infrastructure.middle.elasticsearch.demo;

public class DSLConstans {

    // public static String MAPPING_TEMPLATE = "PUT /hotel"
    // public static String MAPPING_TEMPLATE = "{"
    //         + System.lineSeparator() + "    \"mappings\" : {"
    //         + System.lineSeparator() + "        \"properties\": {"
    //         + System.lineSeparator() + "            \"id\" : {"
    //         + System.lineSeparator() + "              \"type\": \"keyword\""
    //         + System.lineSeparator() + "            },"
    //         + System.lineSeparator() + "            \"name\" : {"
    //         + System.lineSeparator() + "                \"type\" : \"text\","
    //         + System.lineSeparator() + "                \"analyzer\": \"ik_max_word\""
    //         + System.lineSeparator() + "            },"
    //         + System.lineSeparator() + "            \"address\": {"
    //         + System.lineSeparator() + "                \"type\": \"keyword\","
    //         + System.lineSeparator() + "                \"index\": false"
    //         + System.lineSeparator() + "            },"
    //         + System.lineSeparator() + "            \"price\": {"
    //         + System.lineSeparator() + "                \"type\": \"keyword\""
    //         + System.lineSeparator() + "            },"
    //         + System.lineSeparator() + "            \"score\": {"
    //         + System.lineSeparator() + "                \"type\": \"integer\""
    //         + System.lineSeparator() + "            },"
    //         + System.lineSeparator() + "            \"brand\": {"
    //         + System.lineSeparator() + "                \"type\": \"keyword\","
    //         + System.lineSeparator() + "            },"
    //         + System.lineSeparator() + "            \"city\": {"
    //         + System.lineSeparator() + "                \"type\": \"keyword\","
    //         + System.lineSeparator() + "            },"
    //         + System.lineSeparator() + "            \"starName\": {"
    //         + System.lineSeparator() + "                \"type\": \"keyword\","
    //         + System.lineSeparator() + "            },"
    //         + System.lineSeparator() + "            \"business\": {"
    //         + System.lineSeparator() + "                \"type\": \"keyword\","
    //         + System.lineSeparator() + "            },"
    //         + System.lineSeparator() + "            \"location\": {"
    //         + System.lineSeparator() + "                \"type\": \"geo_point\","
    //         + System.lineSeparator() + "            },"
    //         + System.lineSeparator() + "            \"pic\": {"
    //         + System.lineSeparator() + "                \"type\": \"keyword\","
    //         + System.lineSeparator() + "                \"index\": false"
    //         + System.lineSeparator() + "            }"
    //         + System.lineSeparator() + "        }"
    //         + System.lineSeparator() + "    }"
    //         + System.lineSeparator() + "}";

    public static String MAPPING_TEMPLATE = "{\n" +
            "    \"mappings\" : {\n" +
            "        \"properties\": {\n" +
            "            \"id\" : {\n" +
            "              \"type\": \"keyword\"\n" +
            "            },\n" +
            "            \"name\" : {\n" +
            "                \"type\" : \"text\",\n" +
            "                \"analyzer\": \"ik_max_word\",\n" +
            "                \"copy_to\": \"all\"\n" +
            "            },\n" +
            "            \"address\": {\n" +
            "                \"type\": \"keyword\",\n" +
            "                \"index\": false\n" +
            "            },\n" +
            "            \"price\": {\n" +
            "                \"type\": \"keyword\"\n" +
            "            },\n" +
            "            \"score\": {\n" +
            "                \"type\": \"integer\"\n" +
            "            },\n" +
            "            \"brand\": {\n" +
            "                \"type\": \"keyword\",\n" +
            "                \"copy_to\": \"all\"\n" +
            "            },\n" +
            "            \"city\": {\n" +
            "                \"type\": \"keyword\"\n" +
            "            },\n" +
            "            \"starName\": {\n" +
            "                \"type\": \"keyword\"\n" +
            "            },\n" +
            "            \"business\": {\n" +
            "                \"type\": \"keyword\",\n" +
            "                \"copy_to\": \"all\"\n" +
            "            },\n" +
            "            \"location\": {\n" +
            "                \"type\": \"geo_point\"\n" +
            "            },\n" +
            "            \"pic\": {\n" +
            "                \"type\": \"keyword\",\n" +
            "                \"index\": false\n" +
            "            },\n" +
            "            \"all\": {\n" +
            "                \"type\": \"text\",\n" +
            "                \"analyzer\": \"ik_max_word\"\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";
}
