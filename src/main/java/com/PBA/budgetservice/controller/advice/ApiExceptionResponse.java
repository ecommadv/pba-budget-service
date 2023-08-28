package com.PBA.budgetservice.controller.advice;

import java.time.ZonedDateTime;
import java.util.Map;

public record ApiExceptionResponse(ZonedDateTime timestamp, Map<String, String> errors) {}
