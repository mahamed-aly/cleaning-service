package com.cleaningservice.service;

import com.cleaningservice.model.entity.CleaningProfessional;
import com.cleaningservice.model.CleaningProfessionalAvailability;
import com.cleaningservice.model.request.CreateCleaningProfessionalRequest;
import com.cleaningservice.model.request.GetAvailableCleaningProfessionalsRequest;
import com.cleaningservice.model.dto.CleaningProfessionalDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CleaningProfessionalService {
    CleaningProfessionalDTO getCleaningProfessional(Long id);

    CleaningProfessionalDTO createCleaningProfessional(CreateCleaningProfessionalRequest createCleaningProfessionalRequest);

    List<List<CleaningProfessionalAvailability>> getAvailableCleaningProfessionals
            (GetAvailableCleaningProfessionalsRequest getAvailableCleaningProfessionalsRequest);

    List<List<CleaningProfessionalDTO>> getAvailableCleaningProfessionalsWithCount
            (LocalDate date, LocalTime startTime, Integer duration, Integer professionalCount);

    boolean checkCleaningProfessionalHasConflictingBookings
            (CleaningProfessional professional, LocalDate date, LocalTime startTime,
             LocalTime endTime, Optional<Long> excludedBookingId);
}
