package com.cleaningservice.service.impl;

import com.cleaningservice.model.entity.Booking;
import com.cleaningservice.model.entity.Vehicle;
import com.cleaningservice.model.Availability;
import com.cleaningservice.model.CleaningProfessionalAvailability;
import com.cleaningservice.model.request.CreateCleaningProfessionalRequest;
import com.cleaningservice.model.request.GetAvailableCleaningProfessionalsRequest;
import com.cleaningservice.model.dto.CleaningProfessionalDTO;
import com.cleaningservice.model.entity.CleaningProfessional;
import com.cleaningservice.exception.EntityNotFoundException;
import com.cleaningservice.mapper.CleaningProfessionalMapper;
import com.cleaningservice.repository.CleaningProfessionalRepository;
import com.cleaningservice.service.CleaningProfessionalService;
import com.cleaningservice.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
public class CleaningProfessionalServiceImpl implements CleaningProfessionalService {

    private final CleaningProfessionalRepository cleaningProfessionalRepository;
    private final CleaningProfessionalMapper cleaningProfessionalMapper;
    private final VehicleService vehicleService;
    @Value("${constants.work-start-time}")
    private String workStartTime;
    @Value("${constants.work-end-time}")
    private String workEndTime;

    public CleaningProfessionalServiceImpl(CleaningProfessionalRepository cleaningProfessionalRepository,
                                           CleaningProfessionalMapper cleaningProfessionalMapper, @Lazy VehicleService vehicleService) {
        this.cleaningProfessionalRepository = cleaningProfessionalRepository;
        this.cleaningProfessionalMapper = cleaningProfessionalMapper;
        this.vehicleService = vehicleService;
    }

    @Override
    public CleaningProfessionalDTO getCleaningProfessional(final Long id) {
        final CleaningProfessional cleaningProfessional = cleaningProfessionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cleaning professional with id: %s is not found", id)));
        return cleaningProfessionalMapper.toDto(cleaningProfessional);
    }

    @Override
    public CleaningProfessionalDTO createCleaningProfessional(final CreateCleaningProfessionalRequest createCleaningProfessionalRequest) {
        final CleaningProfessionalDTO cleaningProfessionalDTO = cleaningProfessionalMapper.mapRequestToDto(createCleaningProfessionalRequest);
        final CleaningProfessional cleaningProfessional = cleaningProfessionalMapper.toEntity(cleaningProfessionalDTO);
        return cleaningProfessionalMapper.toDto(cleaningProfessionalRepository.save(cleaningProfessional));
    }

    @Override
    public List<List<CleaningProfessionalAvailability>> getAvailableCleaningProfessionals(final GetAvailableCleaningProfessionalsRequest request) {
        final LocalDate date = request.date();
        final LocalTime startTime = request.startTime();
        final Integer duration = request.duration();

        List<List<CleaningProfessionalAvailability>> availableProfessionals = new ArrayList<>();
        // Fetch all vehicles with assigned cleaning professionals
        final List<Vehicle> vehicles = vehicleService.getAll();

        for (Vehicle vehicle : vehicles) {
            List<CleaningProfessionalAvailability> availableForVehicle = new ArrayList<>();
            List<CleaningProfessional> professionals = vehicle.getCleaningProfessionals();

            // Filter professionals based on vehicle and availability
            for (CleaningProfessional professional : professionals) {
                if (startTime == null) {
                    //get CleaningProfessionals availabilities On Date
                    availableForVehicle.add(new CleaningProfessionalAvailability(cleaningProfessionalMapper.toDto(professional),
                            getCleaningProfessionalsAvailabilities(professional, date)));
                } else {
                    //get available cleaning professionals on date and time
                    LocalTime endTime = startTime.plusHours(duration);
                    if (!checkCleaningProfessionalHasConflictingBookings(professional, date, startTime, endTime, Optional.empty())) {
                        availableForVehicle.add(new CleaningProfessionalAvailability(cleaningProfessionalMapper.toDto(professional)));
                    }
                }
            }
            availableProfessionals.add(availableForVehicle);
        }
        return availableProfessionals;
    }

    @Override
    public List<List<CleaningProfessionalDTO>> getAvailableCleaningProfessionalsWithCount
            (final LocalDate date, final LocalTime startTime, final Integer duration, final Integer professionalCount) {

        List<List<CleaningProfessionalDTO>> availableProfessionals = new ArrayList<>();
        final LocalTime endTime = startTime.plusHours(duration);

        // Fetch all vehicles with assigned cleaning professionals
        final List<Vehicle> vehicles = vehicleService.getAll();

        for (Vehicle vehicle : vehicles) {
            List<CleaningProfessionalDTO> availableForVehicle = new ArrayList<>();
            List<CleaningProfessional> professionals = vehicle.getCleaningProfessionals();

            // Filter professionals based on vehicle and availability
            for (CleaningProfessional professional : professionals) {
                if (!checkCleaningProfessionalHasConflictingBookings(professional, date, startTime, endTime, Optional.empty())) {
                    availableForVehicle.add(cleaningProfessionalMapper.toDto(professional));
                }
            }

            // Check if we have enough professionals for the required count
            if (availableForVehicle.size() >= professionalCount) {
                availableProfessionals.add(availableForVehicle);
            }
        }

        // Sort the list in ascending order by size inorder to return the smallest matching list
        // inorder not to block bigger available lists for better booking availabilities
        availableProfessionals.sort(Comparator.comparingInt(List::size));

        return availableProfessionals;
    }

    @Override
    public boolean checkCleaningProfessionalHasConflictingBookings
            (final CleaningProfessional professional, final LocalDate date, final LocalTime startTime,
             final LocalTime endTime, final Optional<Long> excludedBookingId) {
        // Fetch all bookings for the professional on the given date
        final List<Booking> bookings = professional.getBookings()
                .stream()
                .filter(booking -> booking.getDate().equals(date)).filter(booking -> excludedBookingId.isEmpty()
                        || !excludedBookingId.get().equals(booking.getId())).toList();

        for (Booking booking : bookings) {
            LocalTime bookingStart = booking.getStartTime();
            LocalTime bookingEnd = booking.getEndTime();

            // Check for overlap with existing appointments
            if (startTime.isBefore(bookingEnd.plusMinutes(30)) && endTime.isAfter(bookingStart.minusMinutes(30))) {
                return true;
            }
        }
        return false;
    }

    private List<Availability> getCleaningProfessionalsAvailabilities(final CleaningProfessional professional, final LocalDate date) {
        List<Availability> availabilities = new ArrayList<>();

        final LocalTime workStart = LocalTime.parse(workStartTime);
        final LocalTime workEnd = LocalTime.parse(workEndTime);

        // Fetch bookings for the professional on the given date
        final List<Booking> bookings = professional.getBookings()
                .stream()
                .filter(booking -> booking.getDate().isEqual(date))
                .sorted(Comparator.comparing(Booking::getStartTime)).toList();

        // Calculate availabilities between bookings
        LocalTime lastEndTime = workStart;

        for (Booking booking : bookings) {
            // Calculate the end time of availability before this booking, considering the 30-minute break
            LocalTime availabilityEnd = booking.getStartTime();

            // Add an availability slot if there is time between the last booking and the next booking
            if (lastEndTime.isBefore(availabilityEnd)) {
                availabilities.add(new Availability(lastEndTime, availabilityEnd));
            }

            // Update lastEndTime to the end of the current booking
            lastEndTime = booking.getEndTime();
        }

        // Add availability after the last booking until the end of the working hours
        if (lastEndTime.isBefore(workEnd)) {
            availabilities.add(new Availability(lastEndTime, workEnd));
        }
        return availabilities;
    }
}
