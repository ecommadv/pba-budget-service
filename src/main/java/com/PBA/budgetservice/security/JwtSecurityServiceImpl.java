package com.PBA.budgetservice.security;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JwtSecurityServiceImpl implements JwtSecurityService {
    private final SecurityContextHolder contextHolder;

    public JwtSecurityServiceImpl(SecurityContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }

    @Override
    public UUID getCurrentUserUid() {
        return contextHolder.getCurrentUserUid();
    }

    @Override
    public void setCurrentUserUid(UUID uid) {
        contextHolder.setCurrentUserUid(uid);
    }
}
