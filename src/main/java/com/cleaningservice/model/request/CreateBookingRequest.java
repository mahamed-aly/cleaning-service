package com.cleaningservice.model.request;

import com.cleaningservice.validation.ValidCreateBookingRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@ValidCreateBookingRequest
public record CreateBookingRequest(Long id,
                                   @NotNull Long customerId,
                                   @NotNull @Min(1) @Max(3) Integer numberOfCleaningProfessionals,
                                   @NotNull LocalTime startTime,
                                   @NotNull LocalDate date,
                                   @NotNull Integer duration) {
}
