package com.PBA.budgetservice.persistance.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IncomeCategoryDto {
    @Schema(example = "cat1")
    private String name;

    @Schema(example = "4gf23f64-2341-2314-a4fd-5a432g33dab4")
    private UUID uid;
}
