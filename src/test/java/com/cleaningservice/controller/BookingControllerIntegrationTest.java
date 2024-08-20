package com.cleaningservice.controller;

import com.cleaningservice.model.entity.Booking;
import com.cleaningservice.model.entity.Customer;
import com.cleaningservice.model.entity.CleaningProfessional;
import com.cleaningservice.model.entity.Vehicle;
import com.cleaningservice.model.request.CreateBookingRequest;
import com.cleaningservice.model.request.UpdateBookingRequest;
import com.cleaningservice.repository.BookingRepository;
import com.cleaningservice.repository.CustomerRepository;
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
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CleaningProfessionalRepository cleaningProfessionalRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Booking booking;
    private Customer customer;
    private Vehicle vehicle;
    private CleaningProfessional cleaningProfessional;

    @BeforeEach
    void setup() {
        bookingRepository.deleteAll();
        customerRepository.deleteAll();
        cleaningProfessionalRepository.deleteAll();
        vehicleRepository.deleteAll();

        // Create and save customer
        customer = new Customer();
        customer.setName("Jack Peter");
        customerRepository.save(customer);

        // Create and save vehicle
        vehicle = new Vehicle();
        vehicle.setPlateNumber("ABC1234");
        vehicleRepository.save(vehicle);

        // Create and save cleaning professional
        cleaningProfessional = new CleaningProfessional();
        cleaningProfessional.setName("Jane Doe");
        cleaningProfessional.setVehicle(vehicle);
        cleaningProfessionalRepository.save(cleaningProfessional);

        // Create and save booking
        booking = new Booking(
                LocalDate.of(2024, 8, 19),
                LocalTime.of(9, 0),
                2,
                customer,
                Collections.singletonList(cleaningProfessional)
        );
        bookingRepository.save(booking);
    }

    @Test
    void testGetBookingById() throws Exception {
        mockMvc.perform(get("/bookings/{id}", booking.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", is(booking.getDate().toString())))
                .andExpect(jsonPath("$.duration", is(booking.getDuration())))
                .andExpect(jsonPath("$.customer.id", is(booking.getCustomer().getId().intValue())))
                .andExpect(jsonPath("$.cleaningProfessionals[0].id", is(booking.getCleaningProfessionals().get(0).getId().intValue())));
    }

    @Test
    void testGetBookingById_notFound() throws Exception {
        mockMvc.perform(get("/bookings/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBooking() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest
                (null, customer.getId(), 1,
                        LocalTime.of(10, 0), LocalDate.of(2024, 8, 20), 2);


        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.date", is(request.date().toString())))
                .andExpect(jsonPath("$.duration", is(request.duration())))
                .andExpect(jsonPath("$.customer.id", is(request.customerId().intValue())));
    }

    @Test
    void testCreateBooking_noAvailableCleaningProfessionals() throws Exception {
        bookingRepository.deleteAll();
        vehicleRepository.deleteAll();

        CreateBookingRequest request = new CreateBookingRequest
                (null, customer.getId(), 1,
                        LocalTime.of(10, 0), LocalDate.of(2024, 8, 20), 2);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

    }

    @Test
    void testCreateBooking_missingRequiredFields() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest
                (null, null, 1,
                        LocalTime.of(10, 0), LocalDate.of(2024, 8, 20), 2);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testCreateBooking_invalidDuration() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest
                (null, customer.getId(), 1,
                        LocalTime.of(10, 0), LocalDate.of(2024, 8, 20), 3);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateBooking_invalidDate() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest
                (null, customer.getId(), 1,
                        LocalTime.of(10, 0), LocalDate.of(2024, 8, 23), 2);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateBooking_invalidStartTime() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest
                (null, customer.getId(), 1,
                        LocalTime.of(7, 0), LocalDate.of(2024, 8, 19), 2);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBooking() throws Exception {
        UpdateBookingRequest request = new UpdateBookingRequest(LocalTime.of(11, 0),
                LocalDate.of(2024, 8, 19), 2);


        mockMvc.perform(patch("/bookings/{id}", booking.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", is(request.date().toString())))
                .andExpect(jsonPath("$.duration", is(request.duration())));
    }

    @Test
    void testUpdateBooking_missingRequiredFields() throws Exception {
        UpdateBookingRequest request = new UpdateBookingRequest(LocalTime.of(11, 0),
                null, 2);


        mockMvc.perform(patch("/bookings/{id}", booking.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBooking_invalidStartTime() throws Exception {
        UpdateBookingRequest request = new UpdateBookingRequest(LocalTime.of(7, 0),
                LocalDate.of(2024, 8, 19), 2);


        mockMvc.perform(patch("/bookings/{id}", booking.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBooking_invalidDate() throws Exception {
        UpdateBookingRequest request = new UpdateBookingRequest(LocalTime.of(11, 0),
                LocalDate.of(2024, 8, 23), 2);


        mockMvc.perform(patch("/bookings/{id}", booking.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBooking_invalidDuration() throws Exception {
        UpdateBookingRequest request = new UpdateBookingRequest(LocalTime.of(11, 0),
                LocalDate.of(2024, 8, 23), 3);


        mockMvc.perform(patch("/bookings/{id}", booking.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
