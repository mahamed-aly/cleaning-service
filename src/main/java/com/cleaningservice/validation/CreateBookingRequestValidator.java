package com.cleaningservice.validation;

import com.cleaningservice.model.request.CreateBookingRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import static com.cleaningservice.util.ValidationUtil.validateBookingRequest;

public class CreateBookingRequestValidator implements ConstraintValidator<ValidCreateBookingRequest, CreateBookingRequest> {

    @Value("${constants.work-start-time}")
    private String workStartTime;

    @Value("${constants.work-end-time}")
    private String workEndTime;

    @Override
    public boolean isValid(CreateBookingRequest request, ConstraintValidatorContext context) {
        return validateBookingRequest(workStartTime, workEndTime, request.date(), request.duration(), request.startTime(), context);
    }

}
