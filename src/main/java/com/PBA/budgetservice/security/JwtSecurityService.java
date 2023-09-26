package com.PBA.budgetservice.security;

import java.util.UUID;

public interface JwtSecurityService {
    public UUID getCurrentUserUid();
    public void setCurrentUserUid(UUID uid);
}
