package com.cleaningservice.model.request;

import com.cleaningservice.validation.ValidCheckAvailabilityRequest;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@ValidCheckAvailabilityRequest
public record GetAvailableCleaningProfessionalsRequest(@NotNull LocalDate date,
                                                       LocalTime startTime,
                                                       Integer duration) {
}
