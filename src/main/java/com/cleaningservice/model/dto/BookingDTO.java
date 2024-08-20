package com.cleaningservice.model.dto;

import com.cleaningservice.model.entity.CleaningProfessional;
import com.cleaningservice.model.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDTO {
    private Long id;
    private Customer customer;
    private List<CleaningProfessional> cleaningProfessionals;
    private LocalTime startTime;
    private LocalDate date;
    private Integer duration;
}
