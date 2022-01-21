package cz.muni.ics.kypo.training.adaptive.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.IOException;

public class ObjectConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

    public static <T> T convertJsonToObject(String serializedObject, Class<T> objectClass) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(serializedObject, objectClass);
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static <T> T convertJsonToObject(String object, TypeReference<T> typeReference) throws IOException {
        return OBJECT_MAPPER.readValue(object, typeReference);
    }
}
