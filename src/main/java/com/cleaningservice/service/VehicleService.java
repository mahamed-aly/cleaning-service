package com.cleaningservice.service;

import com.cleaningservice.model.request.CreateVehicleRequest;
import com.cleaningservice.model.dto.VehicleDTO;
import com.cleaningservice.model.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleService {
    VehicleDTO getById(Long id);

    Page<Vehicle> getAllPaginated(Pageable pageable);

    VehicleDTO createVehicle(CreateVehicleRequest createVehicleRequest);

    VehicleDTO assignCleaningProfessionalsToVehicle(Long id, List<Long> cleaningProfessionalsIds);

    List<Vehicle> getAll();
}
