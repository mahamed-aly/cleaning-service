package com.cleaningservice.model.dto;

import com.cleaningservice.model.entity.CleaningProfessional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDTO extends RepresentationModel<VehicleDTO> implements Serializable {
    private Long id;
    private String plateNumber;
    private List<CleaningProfessional> cleaningProfessionals;
}
