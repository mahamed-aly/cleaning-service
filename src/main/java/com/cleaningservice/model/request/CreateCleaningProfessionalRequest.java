package com.cleaningservice.model.request;

import jakarta.validation.constraints.NotNull;

public record CreateCleaningProfessionalRequest(Long id, @NotNull String name) {
}
