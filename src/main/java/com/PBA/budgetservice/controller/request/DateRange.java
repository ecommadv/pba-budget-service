package com.PBA.budgetservice.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateRange {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime before;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime after;
}
