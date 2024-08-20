package com.cleaningservice.service;

import com.cleaningservice.model.request.CreateBookingRequest;
import com.cleaningservice.model.request.UpdateBookingRequest;
import com.cleaningservice.model.dto.BookingDTO;

public interface BookingService {

    BookingDTO createBooking(CreateBookingRequest createBookingRequest);

    BookingDTO updateBooking(Long id, UpdateBookingRequest updateBookingRequest);

    BookingDTO getBooking(Long id);
}
