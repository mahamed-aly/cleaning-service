package com.cleaningservice.service;

import com.cleaningservice.exception.EntityNotFoundException;
import com.cleaningservice.mapper.CleaningProfessionalMapper;
import com.cleaningservice.model.Availability;
import com.cleaningservice.model.CleaningProfessionalAvailability;
import com.cleaningservice.model.dto.CleaningProfessionalDTO;
import com.cleaningservice.repository.CleaningProfessionalRepository;
import com.cleaningservice.service.impl.CleaningProfessionalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.cleaningservice.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CleaningProfessionalServiceImplTest {

    @Mock
    private CleaningProfessionalRepository cleaningProfessionalRepository;
    @Mock
    private CleaningProfessionalMapper cleaningProfessionalMapper;
    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private CleaningProfessionalServiceImpl cleaningProfessionalService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(cleaningProfessionalService, "workStartTime",
                WORK_START_TIME);
        ReflectionTestUtils.setField(cleaningProfessionalService, "workEndTime",
                WORK_END_TIME);
    }

    @Test
    void testGetCleaningProfessional_success() {
        // Arrange
        doReturn(Optional.of(CLEANING_PROFESSIONAL)).when(cleaningProfessionalRepository).findById(CLEANING_PROFESSIONAL_ID);
        doReturn(CLEANING_PROFESSIONAL_DTO).when(cleaningProfessionalMapper).toDto(CLEANING_PROFESSIONAL);

        // Act
        CleaningProfessionalDTO actual = cleaningProfessionalService.getCleaningProfessional(CLEANING_PROFESSIONAL_ID);

        // Assert
        verify(cleaningProfessionalRepository).findById(CLEANING_PROFESSIONAL_ID);
        verify(cleaningProfessionalMapper).toDto(CLEANING_PROFESSIONAL);
        assertEquals(CLEANING_PROFESSIONAL_DTO, actual);
    }

    @Test
    void testGetById_throwsEntityNotFoundException() {
        // Arrange
        doReturn(Optional.empty()).when(cleaningProfessionalRepository).findById(CLEANING_PROFESSIONAL_ID);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                cleaningProfessionalService.getCleaningProfessional(CLEANING_PROFESSIONAL_ID)
        );
        assertEquals("Cleaning professional with id: 1 is not found", exception.getMessage());
    }

    @Test
    void testCreateCleaningProfessional() {
        // Arrange
        doReturn(CLEANING_PROFESSIONAL_DTO).when(cleaningProfessionalMapper).mapRequestToDto(CREATE_CLEANING_PROFESSIONAL_REQUEST);
        doReturn(CLEANING_PROFESSIONAL).when(cleaningProfessionalMapper).toEntity(CLEANING_PROFESSIONAL_DTO);
        doReturn(CLEANING_PROFESSIONAL_DTO).when(cleaningProfessionalMapper).toDto(CLEANING_PROFESSIONAL);
        doReturn(CLEANING_PROFESSIONAL).when(cleaningProfessionalRepository).save(CLEANING_PROFESSIONAL);

        // Act
        CleaningProfessionalDTO actual = cleaningProfessionalService.createCleaningProfessional(CREATE_CLEANING_PROFESSIONAL_REQUEST);

        // Assert
        verify(cleaningProfessionalMapper).mapRequestToDto(CREATE_CLEANING_PROFESSIONAL_REQUEST);
        verify(cleaningProfessionalMapper).toEntity(CLEANING_PROFESSIONAL_DTO);
        verify(cleaningProfessionalRepository).save(CLEANING_PROFESSIONAL);
        verify(cleaningProfessionalMapper).toDto(CLEANING_PROFESSIONAL);
        assertEquals(CLEANING_PROFESSIONAL_DTO, actual);
    }

    @Test
    void testGetAvailableCleaningProfessionals_providedDateOnly() {
        // Arrange
        doReturn(List.of(VEHICLE_1)).when(vehicleService).getAll();
        doReturn(CLEANING_PROFESSIONAL_DTO).when(cleaningProfessionalMapper).toDto(CLEANING_PROFESSIONAL);
        Availability availability = new Availability(LocalTime.of(8, 0), LocalTime.of(22, 0));
        CleaningProfessionalAvailability cleaningProfessionalAvailability =
                new CleaningProfessionalAvailability(CLEANING_PROFESSIONAL_DTO, List.of(availability));
        List<List<CleaningProfessionalAvailability>> expected = List.of(List.of(cleaningProfessionalAvailability));

        // Act
        List<List<CleaningProfessionalAvailability>> actual = cleaningProfessionalService
                .getAvailableCleaningProfessionals(GET_AVAILABLE_CLEANING_PROFESSIONALS_REQUEST_DATE_ONLY);

        // Assert
        verify(vehicleService).getAll();
        verify(cleaningProfessionalMapper).toDto(CLEANING_PROFESSIONAL);
        assertEquals(expected, actual);

    }

    @Test
    void testGetAvailableCleaningProfessionals_providedDateAndTime() {
        // Arrange
        doReturn(List.of(VEHICLE_1)).when(vehicleService).getAll();
        doReturn(CLEANING_PROFESSIONAL_DTO).when(cleaningProfessionalMapper).toDto(CLEANING_PROFESSIONAL);
        CleaningProfessionalAvailability cleaningProfessionalAvailability =
                new CleaningProfessionalAvailability(CLEANING_PROFESSIONAL_DTO, null);
        List<List<CleaningProfessionalAvailability>> expected = List.of(List.of(cleaningProfessionalAvailability));

        // Act
        List<List<CleaningProfessionalAvailability>> actual = cleaningProfessionalService
                .getAvailableCleaningProfessionals(GET_AVAILABLE_CLEANING_PROFESSIONALS_REQUEST);

        // Assert
        verify(vehicleService).getAll();
        verify(cleaningProfessionalMapper).toDto(CLEANING_PROFESSIONAL);
        assertEquals(expected, actual);

    }

    @Test
    void testGetAvailableCleaningProfessionalsWithCount() {
        // Arrange
        doReturn(List.of(VEHICLE_1)).when(vehicleService).getAll();
        doReturn(CLEANING_PROFESSIONAL_DTO).when(cleaningProfessionalMapper).toDto(CLEANING_PROFESSIONAL);
        List<List<CleaningProfessionalDTO>> expected = List.of(List.of(CLEANING_PROFESSIONAL_DTO));

        // Act
        List<List<CleaningProfessionalDTO>> actual = cleaningProfessionalService
                .getAvailableCleaningProfessionalsWithCount(BOOKING_DATE, BOOKING_START_TIME, DURATION, NUMBER_OF_CLEANING_PROFESSIONAL);

        // Assert
        verify(vehicleService).getAll();
        verify(cleaningProfessionalMapper).toDto(CLEANING_PROFESSIONAL);
        assertEquals(expected, actual);

    }

    @Test
    void testCheckCleaningProfessionalHasConflictingBookings() {
        assertTrue(cleaningProfessionalService.checkCleaningProfessionalHasConflictingBookings
                (CLEANING_PROFESSIONAL_1, BOOKING_DATE, BOOKING_START_TIME, BOOKING_START_TIME.plusHours(DURATION), Optional.empty()));
    }

}
