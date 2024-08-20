package com.cleaningservice.controller;

import com.cleaningservice.model.request.CreateBookingRequest;
import com.cleaningservice.model.request.UpdateBookingRequest;
import com.cleaningservice.model.dto.BookingDTO;
import com.cleaningservice.service.BookingService;
import com.cleaningservice.util.UriUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    /**
     * GET : get a specific booking by ID
     *
     * @param id booking ID
     * @return http status ok (200) and the required booking details in the body
     */
    @Operation(summary = "Get booking by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        log.info("Rest request to get a booking by id: {}", id);
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    /**
     * POST : create new booking
     *
     * @return the ResponseEntity with http status created (201) and the created booking in the body
     */
    @Operation(summary = "Create booking")
    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody CreateBookingRequest createBookingRequest) {
        log.info("Rest request to create a new booking");
        BookingDTO response = bookingService.createBooking(createBookingRequest);
        return ResponseEntity.created(UriUtil.locationUri(response.getId().toString())).body(response);
    }

    /**
     * PATCH : update booking date / time
     *
     * @param id vehicle ID
     * @return the updated ResponseEntity with http status ok (200)
     */
    @Operation(summary = "Update booking date and time")
    @PatchMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long id, @Valid @RequestBody UpdateBookingRequest updateBookingRequest) {
        log.info("Rest request to update a booking");
        return ResponseEntity.ok(bookingService.updateBooking(id, updateBookingRequest));
    }
}
