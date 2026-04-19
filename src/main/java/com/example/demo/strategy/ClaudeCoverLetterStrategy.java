package com.example.demo.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class ClaudeCoverLetterStrategy implements CoverLetterGenerationStrategy {

    @Value("${anthropic.api.key:}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String generate(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Anthropic API key is not configured. Set ANTHROPIC_API_KEY environment variable.");
        }

        Map<String, Object> requestBody = Map.of(
            "model", "claude-opus-4-7",
            "max_tokens", 2048,
            "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        try {
            String response = restClient.post()
                .uri("https://api.anthropic.com/v1/messages")
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .header("content-type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            return root.path("content").get(0).path("text").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate cover letter via Claude API: " + e.getMessage(), e);
        }
    }
}
