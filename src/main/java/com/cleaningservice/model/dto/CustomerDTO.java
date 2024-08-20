package com.cleaningservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO extends RepresentationModel<CleaningProfessionalDTO> implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
}
