package com.cleaningservice.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cleaning_professionals")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CleaningProfessional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonBackReference
    private Vehicle vehicle;

    @ManyToMany(mappedBy = "cleaningProfessionals")
    @JsonBackReference
    private List<Booking> bookings = new ArrayList<>();
}
