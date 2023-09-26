package com.PBA.budgetservice.gateway;

import java.util.UUID;

public interface UserGateway {
    public UUID getUserUidFromAuthHeader(String authHeader);
}
