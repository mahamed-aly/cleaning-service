package com.cleaningservice.util;

import jakarta.validation.ConstraintValidatorContext;
import lombok.experimental.UtilityClass;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public class ValidationUtil {


    public static boolean validateBookingRequest(String workStartTime,
                                                 String workEndTime,
                                                 LocalDate date,
                                                 Integer duration,
                                                 LocalTime startTime,
                                                 ConstraintValidatorContext context) {

        return validateDate(date, context)
                && validateDuration(duration, context)
                && validateStartTime(workStartTime, startTime, context)
                && validateEndTime(workEndTime, startTime, duration, context);


    }

    public static boolean validateEndTime(String workEndTime, LocalTime startTime, Integer duration, ConstraintValidatorContext context) {
        if (startTime != null && duration != null
                && (startTime.plusHours(duration).isAfter(LocalTime.parse(workEndTime))
                || startTime.plusHours(duration).isBefore(startTime))) {
            updateConstraintValidatorContext(context, "Booking end time is maximum " + workEndTime, "work end time is invalid");
            return false;
        }
        return true;
    }

    public static boolean validateStartTime(String workStartTime, LocalTime startTime, ConstraintValidatorContext context) {
        if (startTime != null && startTime.isBefore(LocalTime.parse(workStartTime))) {
            updateConstraintValidatorContext(context, "Booking start time is " + workStartTime, "work start time is invalid");
            return false;
        }
        return true;
    }

    public static boolean validateDuration(Integer duration, ConstraintValidatorContext context) {
        if (duration != null && duration != 2 && duration != 4) {
            updateConstraintValidatorContext(context, "Booking duration is either 2 or 4 hrs", "duration is invalid");
            return false;
        }
        return true;
    }

    public static boolean validateDate(LocalDate date, ConstraintValidatorContext context) {
        if (date != null && date.getDayOfWeek() == DayOfWeek.FRIDAY) {
            updateConstraintValidatorContext(context, "Friday is not a working day", "date is invalid");
            return false;
        }
        return true;
    }

    public static void updateConstraintValidatorContext(ConstraintValidatorContext context, String msg, String property) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg)
                .addPropertyNode(property)
                .addConstraintViolation();
    }
}
