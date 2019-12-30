package ca.jrvs.apps.twitter.Util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JsonUtil {

    /**
     * Convert a java object to JSON string
     * @param object input object
     * @param prettyJson
     * @param includeNullValues
     * @return JSON String
     * @throws JsonProcessingException
     */
    public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
            throws JsonProcessingException {
        ObjectMapper m =new ObjectMapper();
        if (!includeNullValues) {
            m.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        if (prettyJson) {
            m.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return m.writeValueAsString(object);
    }

    /**
     * Convert a Java object to pretty Json string
     * @param object
     * @return
     */
    public static String toPrettyJson (Object object) throws JsonProcessingException{
        try {
            return toJson(object, true, false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Fail to convert to JSON String", e);
        }
    }

    /**
     * Parse JSON string to a object
     * @param json JSON str
     * @param clazz object class
     * @param <T> type
     * @return object
     * @throws IOException
     */
    public static <T> T toObjectFromJson (String json, Class clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (T) objectMapper.readValue(json,clazz);
    }
}