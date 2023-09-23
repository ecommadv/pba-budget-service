package com.PBA.budgetservice.security.interceptor;

import com.PBA.budgetservice.exceptions.AuthorizationException;
import com.PBA.budgetservice.gateway.UserGateway;
import com.PBA.budgetservice.security.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class JwtValidationInterceptor implements HandlerInterceptor {
    private final UserGateway userGateway;
    private final SecurityContextHolder contextHolder;

    public JwtValidationInterceptor(UserGateway userGateway, SecurityContextHolder contextHolder) {
        this.userGateway = userGateway;
        this.contextHolder = contextHolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new AuthorizationException();
        }

        try {
            UUID userUid = userGateway.getUserUidFromAuthHeader(header);
            contextHolder.setCurrentUserUid(userUid);
        }
        catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AuthorizationException();
            }
        }
        return true;
    }
}
