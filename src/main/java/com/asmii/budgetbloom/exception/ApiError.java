package com.asmii.budgetbloom.exception;

import java.time.LocalDateTime;
import java.util.Map;

/** Structured JSON error body returned for every failure. */
public record ApiError(LocalDateTime timestamp, int status, String error, String message, Map<String, String> fieldErrors) {}
