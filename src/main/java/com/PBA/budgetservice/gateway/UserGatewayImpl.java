package com.PBA.budgetservice.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class UserGatewayImpl implements UserGateway {
    private final WebClient webClient;

    @Value("${auth.get_user_url}")
    private String getUserUrl;

    public UserGatewayImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public UUID getUserUidFromAuthHeader(String authHeader) {
        return webClient.get()
                .uri(getUserUrl)
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(this::extractUid)
                .block();
    }

    private UUID extractUid(JsonNode jsonNode) {
        return UUID.fromString(jsonNode.get("uid").asText());
    }
}
