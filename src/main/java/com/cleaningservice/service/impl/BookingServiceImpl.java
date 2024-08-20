package com.cleaningservice.service.impl;

import com.cleaningservice.model.entity.Booking;
import com.cleaningservice.model.entity.CleaningProfessional;
import com.cleaningservice.model.entity.Customer;
import com.cleaningservice.exception.CannotCreateBookingException;
import com.cleaningservice.exception.CannotUpdateBookingException;
import com.cleaningservice.exception.EntityNotFoundException;
import com.cleaningservice.mapper.BookingMapper;
import com.cleaningservice.mapper.CleaningProfessionalMapper;
import com.cleaningservice.mapper.CustomerMapper;
import com.cleaningservice.model.request.CreateBookingRequest;
import com.cleaningservice.model.request.UpdateBookingRequest;
import com.cleaningservice.model.dto.BookingDTO;
import com.cleaningservice.model.dto.CleaningProfessionalDTO;
import com.cleaningservice.model.dto.CustomerDTO;
import com.cleaningservice.repository.BookingRepository;
import com.cleaningservice.service.BookingService;
import com.cleaningservice.service.CleaningProfessionalService;
import com.cleaningservice.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final CleaningProfessionalService cleaningProfessionalService;
    private final CleaningProfessionalMapper cleaningProfessionalMapper;

    @Override
    public BookingDTO createBooking(final CreateBookingRequest createBookingRequest) {

        final CustomerDTO customerDTO = customerService.getCustomer(createBookingRequest.customerId());
        final Customer customer = customerMapper.toEntity(customerDTO);

        // check the availability of the cleaning professionals
        final List<List<CleaningProfessionalDTO>> cleaningProfessionalsDtosLists =
                cleaningProfessionalService.getAvailableCleaningProfessionalsWithCount
                        (createBookingRequest.date(), createBookingRequest.startTime(), createBookingRequest.duration(),
                                createBookingRequest.numberOfCleaningProfessionals());

        if (cleaningProfessionalsDtosLists.isEmpty()) {
            throw new CannotCreateBookingException("No available cleaning professionals to serve this booking," +
                    " please try different date or time");
        }

        log.info("There are available cleaning professionals");

        final List<CleaningProfessionalDTO> cleaningProfessionalDtos = cleaningProfessionalsDtosLists.get(0);

        final List<CleaningProfessional> cleaningProfessionalsToBeAssigned = cleaningProfessionalDtos.stream()
                .limit(createBookingRequest.numberOfCleaningProfessionals())
                .map(cleaningProfessionalMapper::toEntity)
                .toList();

        final Booking booking = new Booking(createBookingRequest.date(), createBookingRequest.startTime(),
                createBookingRequest.duration(), customer, cleaningProfessionalsToBeAssigned);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    /**
     * The booking update algorithm try at first to check if the current cleaning professionals
     * assigned to the original booking are available at the new date/time if so then they will be reassigned.
     * If they are not available then check the availability of remaining cleaning professionals and assign them to the updated booking
     */
    @Override
    public BookingDTO updateBooking(final Long id, final UpdateBookingRequest updateBookingRequest) {
        final Booking booking = bookingRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Booking with id: %s is not found", id)));

        // Check if all current CleaningProfessionals are free in the new date/time if so assign them to the updated date/time
        final boolean isAllCurrentCleaningProfessionalsAvailable = booking.getCleaningProfessionals()
                .stream()
                .noneMatch(cleaningProfessional ->
                        cleaningProfessionalService
                                .checkCleaningProfessionalHasConflictingBookings(cleaningProfessional,
                                        updateBookingRequest.date(),
                                        updateBookingRequest.startTime(),
                                        updateBookingRequest.startTime().plusHours(updateBookingRequest.duration()), Optional.of(id)));

        if (isAllCurrentCleaningProfessionalsAvailable) {
            log.info("Same cleaning professionals will be assigned to the updated booking");
            booking.setDuration(updateBookingRequest.duration());
            booking.setStartTime(updateBookingRequest.startTime());
            booking.setDate(updateBookingRequest.date());
        }

        // else check the availability of the remaining cleaning professionals and assign them if available
        else {
            log.info("Same cleaning professionals aren't available, will check the availability of other cleaning professionals");
            final List<List<CleaningProfessionalDTO>> cleaningProfessionalsDtosLists = cleaningProfessionalService
                    .getAvailableCleaningProfessionalsWithCount(updateBookingRequest.date(), updateBookingRequest.startTime(),
                            updateBookingRequest.duration(), booking.getCleaningProfessionals().size());

            if (cleaningProfessionalsDtosLists.isEmpty()) {
                throw new CannotUpdateBookingException("This booking can't be updated to selected date / time, please try a different date or time");
            }

            final List<CleaningProfessionalDTO> cleaningProfessionalDtos = cleaningProfessionalsDtosLists.get(0);

            final List<CleaningProfessional> cleaningProfessionalsToBeAssigned = cleaningProfessionalDtos.stream()
                    .limit(booking.getCleaningProfessionals().size())
                    .map(cleaningProfessionalMapper::toEntity)
                    .collect(Collectors.toCollection(ArrayList::new));

            log.info("Other cleaning professionals will be assigned to the updated booking");

            booking.setDate(updateBookingRequest.date());
            booking.setStartTime(updateBookingRequest.startTime());
            booking.setDuration(updateBookingRequest.duration());
            booking.setCleaningProfessionals(cleaningProfessionalsToBeAssigned);
        }
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDTO getBooking(final Long id) {
        final Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Booking with id: %s is not found", id)));
        return bookingMapper.toDto(booking);
    }
}
