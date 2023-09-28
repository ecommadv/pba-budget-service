package com.PBA.budgetservice.gateway;

import com.PBA.budgetservice.persistance.model.GroupLoginInfo;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
public class UserGatewayImpl implements UserGateway {
    private final WebClient webClient;

    @Value("${auth.get_user_url}")
    private String getUserUrl;

    @Value("${auth.get_login_info_url}")
    private String getGroupLoginInfoUrl;

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

    @Override
    public GroupLoginInfo getGroupLoginInfo(String authHeader) {
        return webClient.get()
                .uri(getGroupLoginInfoUrl)
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(this::mapToGroupLoginInfo)
                .block();
    }

    private UUID extractUid(JsonNode jsonNode) {
        return UUID.fromString(jsonNode.get("uid").asText());
    }

    private GroupLoginInfo mapToGroupLoginInfo(JsonNode jsonNode) {
        return GroupLoginInfo.builder()
                .userUid(UUID.fromString(jsonNode.get("userUid").asText()))
                .groupUid(UUID.fromString(jsonNode.get("groupUid").asText()))
                .role(jsonNode.get("role").asText())
                .build();
    }
}
