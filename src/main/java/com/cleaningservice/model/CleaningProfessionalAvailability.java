package com.cleaningservice.model;

import com.cleaningservice.model.dto.CleaningProfessionalDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CleaningProfessionalAvailability {
    private CleaningProfessionalDTO cleaningProfessionalDTO;
    private List<Availability> availabilities;

    public CleaningProfessionalAvailability(CleaningProfessionalDTO cleaningProfessionalDTO) {
        this.cleaningProfessionalDTO = cleaningProfessionalDTO;
    }
}
