package com.cleaningservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CleaningProfessionalDTO extends RepresentationModel<CleaningProfessionalDTO> implements Serializable {
    private Long id;
    private String name;
    private Long vehicleId;
    private List<Long> bookingIds;
}
