package com.cleaningservice.validation;

import jakarta.validation.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CreateBookingRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCreateBookingRequest {
    String message() default "Invalid create booking request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
