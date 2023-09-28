package com.PBA.budgetservice.security;

public interface JwtUtils {
    public String extractTokenFromHeader(String header);
    public String extractTokenType(String token);
}
