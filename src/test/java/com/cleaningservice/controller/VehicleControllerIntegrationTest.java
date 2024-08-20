package com.cleaningservice.controller;

import com.cleaningservice.model.entity.CleaningProfessional;
import com.cleaningservice.model.entity.Vehicle;
import com.cleaningservice.model.request.CreateVehicleRequest;
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

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class VehicleControllerIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CleaningProfessionalRepository cleaningProfessionalRepository;

    private Vehicle vehicle;
    private CleaningProfessional cleaningProfessional;

    @BeforeEach
    void setup() {
        cleaningProfessionalRepository.deleteAll();
        vehicleRepository.deleteAll();

        cleaningProfessional = new CleaningProfessional();
        cleaningProfessional.setName("Johne");
        cleaningProfessionalRepository.save(cleaningProfessional);

        vehicle = new Vehicle();
        vehicle.setPlateNumber("ABC1234");
        vehicleRepository.save(vehicle);
    }

    @Test
    void testGetVehicleById() throws Exception {
        mockMvc.perform(get("/vehicles/{id}", vehicle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber", is(vehicle.getPlateNumber())));
    }

    @Test
    void testGetVehicleById_notFound() throws Exception {
        mockMvc.perform(get("/vehicles/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllVehicles() throws Exception {
        mockMvc.perform(get("/vehicles")
                        .param("pageNumber", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].plateNumber", is(vehicle.getPlateNumber())));
    }

    @Test
    void testCreateVehicle() throws Exception {

        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\": \"ABC1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.plateNumber", is("ABC1234")));
    }

    @Test
    void testCreateVehicle_missingRequiredFields() throws Exception {
        CreateVehicleRequest request = new CreateVehicleRequest(1l, null);

        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\": null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAssignCleanersToVehicle() throws Exception {
        mockMvc.perform(patch("/vehicles/add-cleaning-professionals/{id}", vehicle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(cleaningProfessional.getId()))))
                .andExpect(status().isOk());
    }
}
