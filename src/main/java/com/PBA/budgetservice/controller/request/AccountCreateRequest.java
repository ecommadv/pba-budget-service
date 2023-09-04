package com.PBA.budgetservice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "{userUid.notnull}")
    @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID userUid;

    @NotBlank(message = "{currency.notblank}")
    @Schema(example = "RON")
    private String currency;
}
