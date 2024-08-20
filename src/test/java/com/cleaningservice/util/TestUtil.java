package com.cleaningservice.util;

import com.cleaningservice.model.entity.Booking;
import com.cleaningservice.model.entity.CleaningProfessional;
import com.cleaningservice.model.entity.Customer;
import com.cleaningservice.model.entity.Vehicle;
import com.cleaningservice.model.dto.BookingDTO;
import com.cleaningservice.model.dto.CleaningProfessionalDTO;
import com.cleaningservice.model.dto.CustomerDTO;
import com.cleaningservice.model.dto.VehicleDTO;
import com.cleaningservice.model.request.*;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TestUtil {
    // Constants
    public static final Long VEHICLE_ID = 1L;
    public static final Long BOOKING_ID = 1L;
    public static final Integer NUMBER_OF_CLEANING_PROFESSIONAL = 1;
    public static final LocalDate BOOKING_DATE = LocalDate.of(2024, 8, 25);
    public static final LocalTime BOOKING_START_TIME = LocalTime.of(8, 0);
    public static final LocalTime UPDATED_BOOKING_START_TIME = LocalTime.of(10, 30);
    public static final Integer DURATION = 2;
    public static final Long CLEANING_PROFESSIONAL_ID = 1L;
    public static final Long ANOTHER_CLEANING_PROFESSIONAL_ID = 2L;
    public static final Long CUSTOMER_ID = 1L;
    public static final String ADDRESS = "LA";
    public static final String PHONE_NUMBER = "123456";
    public static final String NAME = "John";
    public static final String PLATE_NUMBER = "123";
    public static final int MAX_NUMBER_OF_CLEANING_PROFESSIONALS_PER_VEHICLE = 5;
    public static final String WORK_START_TIME = "08:00";
    public static final String WORK_END_TIME = "22:00";

    // Entities
    public static final Vehicle VEHICLE = new Vehicle(VEHICLE_ID, PLATE_NUMBER, new ArrayList<>());
    public static final CleaningProfessional CLEANING_PROFESSIONAL = new CleaningProfessional(CLEANING_PROFESSIONAL_ID, NAME, VEHICLE, new ArrayList<>());
    public static final Vehicle VEHICLE_1 = new Vehicle(VEHICLE_ID, PLATE_NUMBER, List.of(CLEANING_PROFESSIONAL));
    public static final CleaningProfessional ANOTHER_CLEANING_PROFESSIONAL = new CleaningProfessional(ANOTHER_CLEANING_PROFESSIONAL_ID, NAME, VEHICLE, new ArrayList<>());
    public static final Customer CUSTOMER = new Customer(CUSTOMER_ID, NAME, ADDRESS, PHONE_NUMBER, new ArrayList<>());
    public static final Booking BOOKING = new Booking(BOOKING_DATE, BOOKING_START_TIME, DURATION, CUSTOMER, List.of(CLEANING_PROFESSIONAL));
    public static final Booking UPDATED_BOOKING = new Booking(BOOKING_DATE, UPDATED_BOOKING_START_TIME, DURATION, CUSTOMER, List.of(CLEANING_PROFESSIONAL));
    public static final Booking UPDATED_BOOKING_ANOTHER_C_P = new Booking(BOOKING_DATE, UPDATED_BOOKING_START_TIME, DURATION, CUSTOMER, List.of(ANOTHER_CLEANING_PROFESSIONAL));
    public static final Booking BOOKING_1 = new Booking(BOOKING_ID, BOOKING_DATE, BOOKING_START_TIME, BOOKING_START_TIME.plusHours(2), DURATION, CUSTOMER, List.of(CLEANING_PROFESSIONAL));
    public static final CleaningProfessional CLEANING_PROFESSIONAL_1 = new CleaningProfessional(CLEANING_PROFESSIONAL_ID, NAME, VEHICLE, List.of(BOOKING_1));
    public static final BookingDTO BOOKING_DTO = new BookingDTO(BOOKING_ID, CUSTOMER, List.of(CLEANING_PROFESSIONAL), BOOKING_START_TIME, BOOKING_DATE, DURATION);
    public static final BookingDTO UPDATED_BOOKING_DTO = new BookingDTO(BOOKING_ID, CUSTOMER, List.of(CLEANING_PROFESSIONAL), UPDATED_BOOKING_START_TIME, BOOKING_DATE, DURATION);
    public static final BookingDTO UPDATED_BOOKING_DTO_ANOTHER_C_P = new BookingDTO(BOOKING_ID, CUSTOMER, List.of(ANOTHER_CLEANING_PROFESSIONAL), UPDATED_BOOKING_START_TIME, BOOKING_DATE, DURATION);

    // DTOs
    public static final VehicleDTO VEHICLE_DTO = new VehicleDTO(VEHICLE_ID, PLATE_NUMBER, List.of());
    public static final CleaningProfessionalDTO CLEANING_PROFESSIONAL_DTO = new CleaningProfessionalDTO(CLEANING_PROFESSIONAL_ID, NAME, null, new ArrayList<>());
    public static final CleaningProfessionalDTO ANOTHER_CLEANING_PROFESSIONAL_DTO = new CleaningProfessionalDTO(ANOTHER_CLEANING_PROFESSIONAL_ID, NAME, null, new ArrayList<>());
    public static final CustomerDTO CUSTOMER_DTO = new CustomerDTO(CUSTOMER_ID, NAME, ADDRESS, PHONE_NUMBER);

    // Requests
    public static final CreateVehicleRequest CREATE_VEHICLE_REQUEST = new CreateVehicleRequest(VEHICLE_ID, PLATE_NUMBER);
    public static final CreateCustomerRequest CREATE_CUSTOMER_REQUEST = new CreateCustomerRequest(CUSTOMER_ID, NAME, ADDRESS, PHONE_NUMBER);
    public static final CreateBookingRequest CREATE_BOOKING_REQUEST = new CreateBookingRequest(BOOKING_ID, CUSTOMER_ID, NUMBER_OF_CLEANING_PROFESSIONAL, BOOKING_START_TIME, BOOKING_DATE, DURATION);
    public static final UpdateBookingRequest UPDATE_BOOKING_REQUEST = new UpdateBookingRequest(UPDATED_BOOKING_START_TIME, BOOKING_DATE, DURATION);
    public static final CreateCleaningProfessionalRequest CREATE_CLEANING_PROFESSIONAL_REQUEST = new CreateCleaningProfessionalRequest(CLEANING_PROFESSIONAL_ID, NAME);
    public static final GetAvailableCleaningProfessionalsRequest GET_AVAILABLE_CLEANING_PROFESSIONALS_REQUEST_DATE_ONLY = new GetAvailableCleaningProfessionalsRequest(BOOKING_DATE, null, null);
    public static final GetAvailableCleaningProfessionalsRequest GET_AVAILABLE_CLEANING_PROFESSIONALS_REQUEST = new GetAvailableCleaningProfessionalsRequest(BOOKING_DATE, BOOKING_START_TIME, DURATION);
}
