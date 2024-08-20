package com.cleaningservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UpdateBookingRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUpdateBookingRequest {
    String message() default "Invalid update booking request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
