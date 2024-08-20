package com.cleaningservice.mapper;

import com.cleaningservice.model.request.CreateVehicleRequest;
import com.cleaningservice.model.dto.VehicleDTO;
import com.cleaningservice.model.entity.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper extends TwoWayMapper<Vehicle, VehicleDTO> {
    VehicleDTO mapRequestToDto(CreateVehicleRequest createVehicleRequest);
}
