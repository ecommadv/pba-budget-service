package com.PBA.budgetservice.gateway;

import com.PBA.budgetservice.persistance.model.GroupLoginInfo;

import java.util.UUID;

public interface UserGateway {
    public UUID getUserUidFromAuthHeader(String authHeader);
    public GroupLoginInfo getGroupLoginInfo(String header);
}
