package com.PBA.budgetservice.security.interceptor;

import com.PBA.budgetservice.exceptions.AuthorizationException;
import com.PBA.budgetservice.gateway.UserGateway;
import com.PBA.budgetservice.persistance.model.GroupLoginInfo;
import com.PBA.budgetservice.security.JwtUtils;
import com.PBA.budgetservice.security.SecurityContextHolder;
import com.PBA.budgetservice.security.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class JwtHeaderHandlerInterceptor implements HandlerInterceptor {
    private final UserGateway userGateway;
    private final SecurityContextHolder contextHolder;
    private final JwtUtils jwtUtils;

    public JwtHeaderHandlerInterceptor(UserGateway userGateway, SecurityContextHolder contextHolder, JwtUtils jwtUtils) {
        this.userGateway = userGateway;
        this.contextHolder = contextHolder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        String token = jwtUtils.extractTokenFromHeader(header);
        String tokenType = jwtUtils.extractTokenType(token);
        Map<String, Consumer<String>> tokenHandler = Map.of(
                TokenType.USER, this::handleUserHeader,
                TokenType.GROUP, this::handleGroupHeader
        );
        try {
            tokenHandler.get(tokenType).accept(header);
        }
        catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AuthorizationException();
            }
        }
        return true;
    }

    private void handleUserHeader(String header) {
        UUID userUid = userGateway.getUserUidFromAuthHeader(header);
        contextHolder.setCurrentUserUid(userUid);
        contextHolder.setCurrentTokenType(TokenType.USER);
    }

    private void handleGroupHeader(String header) {
        GroupLoginInfo groupLoginInfo = userGateway.getGroupLoginInfo(header);
        contextHolder.setGroupLoginInfo(groupLoginInfo);
        contextHolder.setCurrentTokenType(TokenType.GROUP);
    }
}
