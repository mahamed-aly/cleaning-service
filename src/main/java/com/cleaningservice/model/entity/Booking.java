package com.cleaningservice.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "DATE")
    private LocalDate date;

    @Column(columnDefinition = "TIME")
    private LocalTime startTime;

    @Column(columnDefinition = "TIME")
    private LocalTime endTime;

    private Integer duration;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;
    @ManyToMany
    @JoinTable(
            name = "booking_cleaning_professional",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "cleaning_professional_id")
    )
    @JsonManagedReference
    private List<CleaningProfessional> cleaningProfessionals = new ArrayList<>();

    public Booking(LocalDate date, LocalTime startTime, Integer duration, Customer customer,
                   List<CleaningProfessional> cleaningProfessionals) {
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
        this.customer = customer;
        this.cleaningProfessionals = cleaningProfessionals;
    }

    @PrePersist
    @PreUpdate
    public void calculateEndTime() {
        this.endTime = this.startTime.plusHours(this.duration);
    }
}
