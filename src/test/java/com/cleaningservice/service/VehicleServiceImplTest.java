package com.cleaningservice.service;

import com.cleaningservice.model.entity.Vehicle;
import com.cleaningservice.exception.EntityNotFoundException;
import com.cleaningservice.exception.InvalidArgumentsException;
import com.cleaningservice.mapper.CleaningProfessionalMapper;
import com.cleaningservice.mapper.VehicleMapper;
import com.cleaningservice.model.dto.VehicleDTO;
import com.cleaningservice.repository.VehicleRepository;
import com.cleaningservice.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static com.cleaningservice.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private VehicleMapper vehicleMapper;
    @Mock
    private CleaningProfessionalService cleaningProfessionalService;
    @Mock
    private CleaningProfessionalMapper cleaningProfessionalMapper;
    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(vehicleService, "maxNumberOfCLeaningProfessionalsPerVehicle",
                MAX_NUMBER_OF_CLEANING_PROFESSIONALS_PER_VEHICLE);
    }

    @Test
    void testGetById_success() {
        // Arrange
        doReturn(Optional.of(VEHICLE)).when(vehicleRepository).findById(VEHICLE_ID);
        doReturn(VEHICLE_DTO).when(vehicleMapper).toDto(VEHICLE);

        // Act
        VehicleDTO actual = vehicleService.getById(VEHICLE_ID);

        // Assert
        verify(vehicleRepository).findById(VEHICLE_ID);
        verify(vehicleMapper).toDto(VEHICLE);
        assertEquals(VEHICLE_DTO, actual);
    }

    @Test
    void testGetById_throwsEntityNotFoundException() {
        // Arrange
        doReturn(Optional.empty()).when(vehicleRepository).findById(VEHICLE_ID);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                vehicleService.getById(VEHICLE_ID)
        );
        assertEquals("Vehicle with id: 1 is not found", exception.getMessage());
    }

    @Test
    void testGetAllPaginated() {
        // Arrange
        Pageable pageable = PageRequest.of(1, 2);
        Page<Vehicle> expected = new PageImpl<>(List.of(VEHICLE));
        doReturn(expected).when(vehicleRepository).findAll(pageable);

        // Act
        Page<Vehicle> actual = vehicleService.getAllPaginated(pageable);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testCreateVehicle() {
        // Arrange
        doReturn(VEHICLE_DTO).when(vehicleMapper).mapRequestToDto(CREATE_VEHICLE_REQUEST);
        doReturn(VEHICLE).when(vehicleMapper).toEntity(VEHICLE_DTO);
        doReturn(VEHICLE_DTO).when(vehicleMapper).toDto(VEHICLE);

        // Act
        VehicleDTO actual = vehicleService.createVehicle(CREATE_VEHICLE_REQUEST);

        // Assert
        verify(vehicleMapper).mapRequestToDto(CREATE_VEHICLE_REQUEST);
        verify(vehicleMapper).toEntity(VEHICLE_DTO);
        verify(vehicleRepository).save(VEHICLE);
        verify(vehicleMapper).toDto(VEHICLE);
        assertEquals(VEHICLE_DTO, actual);
    }

    @Test
    void testAssignCleaningProfessionalsToVehicle_success() {
        // Arrange
        List<Long> cleaningProfessionalIds = List.of(CLEANING_PROFESSIONAL.getId());
        doReturn(Optional.of(VEHICLE)).when(vehicleRepository).findById(VEHICLE_ID);
        doReturn(CLEANING_PROFESSIONAL_DTO).when(cleaningProfessionalService).getCleaningProfessional(CLEANING_PROFESSIONAL_ID);
        doReturn(CLEANING_PROFESSIONAL).when(cleaningProfessionalMapper).toEntity(CLEANING_PROFESSIONAL_DTO);
        doReturn(VEHICLE_DTO).when(vehicleMapper).toDto(VEHICLE);

        // Act
        vehicleService.assignCleaningProfessionalsToVehicle(VEHICLE_ID, cleaningProfessionalIds);

        // Assert
        verify(vehicleRepository).findById(VEHICLE_ID);
        verify(cleaningProfessionalService).getCleaningProfessional(CLEANING_PROFESSIONAL_ID);
        verify(cleaningProfessionalMapper).toEntity(CLEANING_PROFESSIONAL_DTO);
        verify(vehicleMapper).toDto(VEHICLE);
        assertEquals(1, VEHICLE.getCleaningProfessionals().size());
    }

    @Test
    void testAssignCleaningProfessionalsToVehicle_throwsEntityNotFoundException() {
        // Arrange
        List<Long> cleaningProfessionalIds = List.of(CLEANING_PROFESSIONAL.getId());
        doReturn(Optional.empty()).when(vehicleRepository).findById(VEHICLE_ID);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                vehicleService.assignCleaningProfessionalsToVehicle(VEHICLE_ID, cleaningProfessionalIds));
        assertEquals("Vehicle with ID: 1 doesn't exist", exception.getMessage());
    }

    @Test
    void testAssignCleaningProfessionalsToVehicle_throwsInvalidArgumentsException() {
        // Arrange
        List<Long> cleaningProfessionalIds = List.of(1L, 2L, 3L, 4L, 5L, 6L);
        doReturn(Optional.of(VEHICLE)).when(vehicleRepository).findById(VEHICLE_ID);

        // Act & Assert
        InvalidArgumentsException exception = assertThrows(InvalidArgumentsException.class, () ->
                vehicleService.assignCleaningProfessionalsToVehicle(VEHICLE_ID, cleaningProfessionalIds));
        assertEquals("Maximum number of cleaning professionals per vehicle is 5", exception.getMessage());
    }

    @Test
    void testGetAll() {
        // Arrange
        doReturn(List.of(VEHICLE)).when(vehicleRepository).findAll();

        // Act
        List<Vehicle> actual = vehicleService.getAll();

        // Assert
        assertEquals(List.of(VEHICLE), actual);
    }
}
