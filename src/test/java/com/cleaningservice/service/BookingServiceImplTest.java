package com.cleaningservice.service;

import com.cleaningservice.model.entity.Booking;
import com.cleaningservice.model.entity.CleaningProfessional;
import com.cleaningservice.exception.CannotCreateBookingException;
import com.cleaningservice.exception.CannotUpdateBookingException;
import com.cleaningservice.exception.EntityNotFoundException;
import com.cleaningservice.mapper.BookingMapper;
import com.cleaningservice.mapper.CleaningProfessionalMapper;
import com.cleaningservice.mapper.CustomerMapper;
import com.cleaningservice.model.dto.BookingDTO;
import com.cleaningservice.repository.BookingRepository;
import com.cleaningservice.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.cleaningservice.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private CustomerService customerService;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private CleaningProfessionalService cleaningProfessionalService;
    @Mock
    private CleaningProfessionalMapper cleaningProfessionalMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;


    @Test
    void testGetBooking_success() {
        // Arrange
        doReturn(Optional.of(BOOKING)).when(bookingRepository).findById(BOOKING_ID);
        doReturn(BOOKING_DTO).when(bookingMapper).toDto(BOOKING);

        // Act
        BookingDTO actual = bookingService.getBooking(BOOKING_ID);

        // Assert
        verify(bookingRepository).findById(BOOKING_ID);
        verify(bookingMapper).toDto(BOOKING);
        assertEquals(BOOKING_DTO, actual);

    }

    @Test
    void testGetBooking_throwsEntityNotFoundException() {
        // Arrange
        doReturn(Optional.empty()).when(bookingRepository).findById(BOOKING_ID);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                bookingService.getBooking(BOOKING_ID)
        );
        assertEquals("Booking with id: 1 is not found", exception.getMessage());
    }

    @Test
    void testCreateBooking_availableCleaningProfessionals() {
        // Arrange
        doReturn(CUSTOMER_DTO).when(customerService).getCustomer(CUSTOMER_ID);
        doReturn(CUSTOMER).when(customerMapper).toEntity(CUSTOMER_DTO);
        doReturn(List.of(List.of(CLEANING_PROFESSIONAL_DTO))).when(cleaningProfessionalService)
                .getAvailableCleaningProfessionalsWithCount(BOOKING_DATE,
                        BOOKING_START_TIME, DURATION, NUMBER_OF_CLEANING_PROFESSIONAL);
        doReturn(CLEANING_PROFESSIONAL).when(cleaningProfessionalMapper).toEntity(CLEANING_PROFESSIONAL_DTO);
        doReturn(BOOKING_DTO).when(bookingMapper).toDto(BOOKING);
        doReturn(BOOKING).when(bookingRepository).save(any(Booking.class));

        // Act
        BookingDTO actual = bookingService.createBooking(CREATE_BOOKING_REQUEST);

        // Assert
        verify(customerService).getCustomer(CUSTOMER_ID);
        verify(customerMapper).toEntity(CUSTOMER_DTO);
        verify(cleaningProfessionalService).getAvailableCleaningProfessionalsWithCount(BOOKING_DATE,
                BOOKING_START_TIME, DURATION, NUMBER_OF_CLEANING_PROFESSIONAL);
        verify(cleaningProfessionalMapper).toEntity(CLEANING_PROFESSIONAL_DTO);
        verify(bookingMapper).toDto(BOOKING);
        assertEquals(BOOKING_DTO, actual);

    }

    @Test
    void testCreateBooking_throwsCannotCreateBookingException() {
        // Arrange
        doReturn(CUSTOMER_DTO).when(customerService).getCustomer(CUSTOMER_ID);
        doReturn(CUSTOMER).when(customerMapper).toEntity(CUSTOMER_DTO);
        doReturn(List.of()).when(cleaningProfessionalService)
                .getAvailableCleaningProfessionalsWithCount(BOOKING_DATE,
                        BOOKING_START_TIME, DURATION, NUMBER_OF_CLEANING_PROFESSIONAL);

        // Act & Assert
        CannotCreateBookingException exception = assertThrows(CannotCreateBookingException.class, () ->
                bookingService.createBooking(CREATE_BOOKING_REQUEST)
        );
        assertEquals("No available cleaning professionals to serve this booking," +
                " please try different date or time", exception.getMessage());

    }

    @Test
    void testUpdateBooking_availableSameCleaningProfessionals() {
        // Arrange
        doReturn(Optional.of(BOOKING)).when(bookingRepository).findById(BOOKING_ID);
        doReturn(false).when(cleaningProfessionalService)
                .checkCleaningProfessionalHasConflictingBookings(any(CleaningProfessional.class),
                        any(LocalDate.class), any(LocalTime.class), any(LocalTime.class),
                        any(Optional.class));
        doReturn(UPDATED_BOOKING_DTO).when(bookingMapper).toDto(UPDATED_BOOKING);
        doReturn(UPDATED_BOOKING).when(bookingRepository).save(any(Booking.class));

        // Act
        BookingDTO actual = bookingService.updateBooking(BOOKING_ID, UPDATE_BOOKING_REQUEST);

        // Assert
        verify(bookingRepository).findById(BOOKING_ID);
        verify(bookingMapper).toDto(UPDATED_BOOKING);
        assertEquals(UPDATED_BOOKING_DTO, actual);
    }

    @Test
    void testUpdateBooking_availableOtherCleaningProfessionals() {
        // Arrange
        doReturn(Optional.of(BOOKING)).when(bookingRepository).findById(BOOKING_ID);
        doReturn(true).when(cleaningProfessionalService)
                .checkCleaningProfessionalHasConflictingBookings(CLEANING_PROFESSIONAL,
                        BOOKING_DATE, UPDATED_BOOKING_START_TIME, UPDATED_BOOKING_START_TIME.plusHours(DURATION),
                        Optional.of(BOOKING_ID));
        doReturn(List.of(List.of(ANOTHER_CLEANING_PROFESSIONAL_DTO))).when(cleaningProfessionalService)
                .getAvailableCleaningProfessionalsWithCount(BOOKING_DATE,
                        UPDATED_BOOKING_START_TIME, DURATION, NUMBER_OF_CLEANING_PROFESSIONAL);
        doReturn(ANOTHER_CLEANING_PROFESSIONAL).when(cleaningProfessionalMapper).toEntity(ANOTHER_CLEANING_PROFESSIONAL_DTO);

        doReturn(UPDATED_BOOKING_DTO_ANOTHER_C_P).when(bookingMapper).toDto(UPDATED_BOOKING_ANOTHER_C_P);
        doReturn(UPDATED_BOOKING_ANOTHER_C_P).when(bookingRepository).save(UPDATED_BOOKING_ANOTHER_C_P);

        // Act
        BookingDTO actual = bookingService.updateBooking(BOOKING_ID, UPDATE_BOOKING_REQUEST);

        // Assert
        verify(bookingRepository).findById(BOOKING_ID);
        verify(cleaningProfessionalService).checkCleaningProfessionalHasConflictingBookings(CLEANING_PROFESSIONAL,
                BOOKING_DATE, UPDATED_BOOKING_START_TIME, UPDATED_BOOKING_START_TIME.plusHours(DURATION),
                Optional.of(BOOKING_ID));
        verify(cleaningProfessionalService).getAvailableCleaningProfessionalsWithCount(BOOKING_DATE,
                UPDATED_BOOKING_START_TIME, DURATION, NUMBER_OF_CLEANING_PROFESSIONAL);
        verify(cleaningProfessionalMapper).toEntity(ANOTHER_CLEANING_PROFESSIONAL_DTO);

        verify(bookingMapper).toDto(UPDATED_BOOKING_ANOTHER_C_P);
        verify(bookingRepository).save(UPDATED_BOOKING_ANOTHER_C_P);
        assertEquals(UPDATED_BOOKING_DTO_ANOTHER_C_P, actual);
    }

    @Test
    void testUpdateBooking_throwsCannotUpdateBookingException() {
        // Arrange
        doReturn(Optional.of(BOOKING)).when(bookingRepository).findById(BOOKING_ID);
        doReturn(true).when(cleaningProfessionalService)
                .checkCleaningProfessionalHasConflictingBookings(any(CleaningProfessional.class),
                        any(LocalDate.class), any(LocalTime.class), any(LocalTime.class),
                        any(Optional.class));
        doReturn(List.of()).when(cleaningProfessionalService)
                .getAvailableCleaningProfessionalsWithCount(BOOKING_DATE,
                        UPDATED_BOOKING_START_TIME, DURATION, NUMBER_OF_CLEANING_PROFESSIONAL);

        // Act & Assert
        CannotUpdateBookingException exception = assertThrows(CannotUpdateBookingException.class, () ->
                bookingService.updateBooking(BOOKING_ID, UPDATE_BOOKING_REQUEST)
        );
        assertEquals("This booking can't be updated to selected date / time, please try a different date or time",
                exception.getMessage());

    }

    @Test
    void testUpdateBooking_throwsEntityNotFoundException() {
        // Arrange
        doReturn(Optional.empty()).when(bookingRepository).findById(BOOKING_ID);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                bookingService.updateBooking(BOOKING_ID, UPDATE_BOOKING_REQUEST)
        );
        assertEquals("Booking with id: 1 is not found", exception.getMessage());
    }
}
