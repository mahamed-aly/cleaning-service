package com.cleaningservice.validation;

import com.cleaningservice.model.request.GetAvailableCleaningProfessionalsRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import static com.cleaningservice.util.ValidationUtil.*;

public class CheckAvailabilityRequestValidator implements ConstraintValidator<ValidCheckAvailabilityRequest, GetAvailableCleaningProfessionalsRequest> {
    @Value("${constants.work-start-time}")
    private String workStartTime;

    @Value("${constants.work-end-time}")
    private String workEndTime;

    @Override
    public boolean isValid(GetAvailableCleaningProfessionalsRequest request, ConstraintValidatorContext context) {
        if (!validateDate(request.date(), context)) {
            return false;
        }

        if (request.startTime() != null && request.duration() != null
                && !(validateDuration(request.duration(), context)
                && validateStartTime(workStartTime, request.startTime(), context)
                && validateEndTime(workEndTime, request.startTime(), request.duration(), context))) {

            return false;
        }

        if ((request.startTime() != null && request.duration() == null) || (request.startTime() == null && request.duration() != null)) {
            updateConstraintValidatorContext(context, "both StartTime and duration should be provided.", "missing duration or startTime");
            return false;
        }

        return true;
    }
}
