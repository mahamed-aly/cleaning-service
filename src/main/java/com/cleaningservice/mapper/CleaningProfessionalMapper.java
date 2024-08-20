package com.cleaningservice.mapper;

import com.cleaningservice.model.entity.Booking;
import com.cleaningservice.model.request.CreateCleaningProfessionalRequest;
import com.cleaningservice.model.dto.CleaningProfessionalDTO;
import com.cleaningservice.model.entity.CleaningProfessional;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CleaningProfessionalMapper extends TwoWayMapper<CleaningProfessional, CleaningProfessionalDTO> {
    CleaningProfessionalDTO mapRequestToDto(CreateCleaningProfessionalRequest createCleaningProfessionalRequest);

    @Override
    default CleaningProfessionalDTO toDto(CleaningProfessional entity) {
        if (entity == null) {
            return null;
        }

        CleaningProfessionalDTO dto = new CleaningProfessionalDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setVehicleId(entity.getVehicle() != null ? entity.getVehicle().getId() : null);
        dto.setBookingIds(entity.getBookings().stream()
                .map(Booking::getId)
                .collect(Collectors.toList()));

        return dto;
    }
}
