package com.cleaningservice.model.request;

import jakarta.validation.constraints.NotNull;

public record CreateVehicleRequest(Long id, @NotNull String plateNumber) {
}
