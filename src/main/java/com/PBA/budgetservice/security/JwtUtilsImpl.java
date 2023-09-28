package com.PBA.budgetservice.security;

import com.PBA.budgetservice.exceptions.AuthorizationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class JwtUtilsImpl implements JwtUtils {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Override
    public String extractTokenFromHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new AuthorizationException();
        }
        return header.substring(7);
    }

    @Override
    public String extractTokenType(String token) {
        return this.extractClaim(token, claims -> claims.get("token_type").toString());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = this.extractAllClaims(token);
            return claimsResolver.apply(claims);
        }
        catch(Exception e) {
            throw new AuthorizationException();
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch(Exception e) {
            throw new AuthorizationException();
        }
    }
}
