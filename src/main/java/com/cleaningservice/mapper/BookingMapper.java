package com.cleaningservice.mapper;

import com.cleaningservice.model.entity.Booking;
import com.cleaningservice.model.request.CreateBookingRequest;
import com.cleaningservice.model.dto.BookingDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper extends TwoWayMapper<Booking, BookingDTO> {
    BookingDTO mapRequestToDto(CreateBookingRequest createBookingRequest);
}
