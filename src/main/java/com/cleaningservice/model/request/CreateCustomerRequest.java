package com.cleaningservice.model.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record CreateCustomerRequest(@Nullable Long id,
                                    @NotNull String name,
                                    @NotNull String address,
                                    @NotNull String phoneNumber) {
}
