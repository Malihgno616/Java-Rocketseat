package com.rocketseat.createUrlShortner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
//import java.util.Objects;

public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, String> handleRequest(Map<String, Object> stringObjectMap, Context context) {
        context.getLogger().log("Received input: " + stringObjectMap);

        String body = (stringObjectMap.get("body") != null) ? stringObjectMap.get("body").toString() : null;

        if (body == null) {
            throw new RuntimeException("Body is null. Please provide a valid input.");
        }

        Map<String, String> bodyMap;
        try {
            bodyMap = objectMapper.readValue(body, Map.class);
        } catch(Exception exception) {
            context.getLogger().log("ERROR parsing JSON body: " + exception.getMessage());
            throw new RuntimeException("ERROR parsing JSON body", exception);
        }

        String originalUrl = bodyMap.get("originalUrl");
        String expirationTime = bodyMap.get("expirationTime");

        if (originalUrl == null || expirationTime == null) {
            throw new RuntimeException("Missing required fields: 'originalUrl' or 'expirationTime'");
        }

        String shortUrlCode = UUID.randomUUID().toString().substring(0,8);

        Map<String, String> response = new HashMap<>();
        response.put("code", shortUrlCode);

        context.getLogger().log("Generated short URL code: " + shortUrlCode);

        return response;
    }
}