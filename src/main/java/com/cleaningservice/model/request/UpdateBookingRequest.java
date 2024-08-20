package com.cleaningservice.model.request;

import com.cleaningservice.validation.ValidUpdateBookingRequest;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@ValidUpdateBookingRequest
public record UpdateBookingRequest(@NotNull LocalTime startTime,
                                   @NotNull LocalDate date,
                                   @NotNull Integer duration) {
}
