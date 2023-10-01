package com.PBA.budgetservice.security;

import com.PBA.budgetservice.persistance.model.GroupLoginInfo;

import java.util.UUID;

public interface JwtSecurityService {
    public UUID getCurrentUserUid();
    public void setCurrentUserUid(UUID uid);
    public GroupLoginInfo getCurrentGroupLoginInfo();
    public String getCurrentTokenType();
    public UUID getCurrentAccountOwnerUid();
    public void validateHasPermission(Permission permission);
}
