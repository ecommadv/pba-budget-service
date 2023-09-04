package com.PBA.budgetservice.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountCreateRequest {
    private UUID userUid;
    private String currency;
}
