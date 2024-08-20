package com.cleaningservice.controller;

import com.cleaningservice.model.entity.CleaningProfessional;
import com.cleaningservice.model.entity.Vehicle;
import com.cleaningservice.model.request.CreateCleaningProfessionalRequest;
import com.cleaningservice.model.request.GetAvailableCleaningProfessionalsRequest;
import com.cleaningservice.repository.BookingRepository;
import com.cleaningservice.repository.CleaningProfessionalRepository;
import com.cleaningservice.repository.VehicleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class CleaningProfessionalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CleaningProfessionalRepository cleaningProfessionalRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CleaningProfessional cleaningProfessional;
    private Vehicle vehicle;

    @BeforeEach
    void setup() {
        bookingRepository.deleteAll();
        cleaningProfessionalRepository.deleteAll();
        vehicleRepository.deleteAll();

        vehicle = new Vehicle();
        vehicle.setPlateNumber("ABC1234");
        vehicleRepository.save(vehicle);

        cleaningProfessional = new CleaningProfessional();
        cleaningProfessional.setName("Jack Peter");
        cleaningProfessional.setVehicle(vehicle);
        cleaningProfessionalRepository.save(cleaningProfessional);
    }

    @Test
    void testGetCleaningProfessionalById() throws Exception {
        mockMvc.perform(get("/cleaning-professionals/{id}", cleaningProfessional.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(cleaningProfessional.getName())));
    }

    @Test
    void testGetAvailableCleaningProfessionals() throws Exception {
        GetAvailableCleaningProfessionalsRequest request = new GetAvailableCleaningProfessionalsRequest(
                LocalDate.of(2024, 8, 21), LocalTime.of(9, 0), 2
        );

        mockMvc.perform(get("/cleaning-professionals/available-cleaning-professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetAvailableCleaningProfessionals_missingRequiredFields() throws Exception {
        GetAvailableCleaningProfessionalsRequest request = new GetAvailableCleaningProfessionalsRequest(
                null, LocalTime.of(9, 0), 2
        );

        mockMvc.perform(get("/cleaning-professionals/available-cleaning-professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCleaningProfessional() throws Exception {
        CreateCleaningProfessionalRequest request = new CreateCleaningProfessionalRequest(null, "Jack");

        mockMvc.perform(post("/cleaning-professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Jack")));
    }

    @Test
    void testCreateCleaningProfessional_missingRequiredFields() throws Exception {
        CreateCleaningProfessionalRequest request = new CreateCleaningProfessionalRequest(null, null);

        mockMvc.perform(post("/cleaning-professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCleaningProfessionalById_notFound() throws Exception {
        mockMvc.perform(get("/cleaning-professionals/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
