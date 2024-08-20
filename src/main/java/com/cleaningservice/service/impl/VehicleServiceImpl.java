package com.cleaningservice.service.impl;

import com.cleaningservice.model.request.CreateVehicleRequest;
import com.cleaningservice.model.dto.VehicleDTO;
import com.cleaningservice.model.entity.CleaningProfessional;
import com.cleaningservice.model.entity.Vehicle;
import com.cleaningservice.exception.EntityNotFoundException;
import com.cleaningservice.exception.InvalidArgumentsException;
import com.cleaningservice.mapper.CleaningProfessionalMapper;
import com.cleaningservice.mapper.VehicleMapper;
import com.cleaningservice.repository.VehicleRepository;
import com.cleaningservice.service.CleaningProfessionalService;
import com.cleaningservice.service.VehicleService;
import com.cleaningservice.util.UriUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final CleaningProfessionalService cleaningProfessionalService;
    private final CleaningProfessionalMapper cleaningProfessionalMapper;

    @Value("${constants.max-number-of-cleaning-professionals-per-vehicle}")
    private int maxNumberOfCLeaningProfessionalsPerVehicle;

    @Override
    @Transactional(readOnly = true)
    public VehicleDTO getById(final Long id) {
        final Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Vehicle with id: %s is not found", id)));
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);
        final List<Link> links = UriUtil.addLinks(id, VehicleDTO.class);
        vehicleDTO.add(links);
        return vehicleDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Vehicle> getAllPaginated(Pageable pageable) {
        return vehicleRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public VehicleDTO createVehicle(final CreateVehicleRequest createVehicleRequest) {
        final VehicleDTO vehicleDTO = vehicleMapper.mapRequestToDto(createVehicleRequest);
        final Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public VehicleDTO assignCleaningProfessionalsToVehicle(final Long id, final List<Long> cleaningProfessionalsIds) {
        final Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Vehicle with ID: " + id + " doesn't exist"));

        if (cleaningProfessionalsIds.size() + vehicle.getCleaningProfessionals()
                .size() > maxNumberOfCLeaningProfessionalsPerVehicle) {
            throw new InvalidArgumentsException("Maximum number of cleaning professionals per vehicle is "
                    + maxNumberOfCLeaningProfessionalsPerVehicle);
        }

        final List<CleaningProfessional> cleaningProfessionals =
                cleaningProfessionalsIds.stream()
                        .map(cleaningProfessionalService::getCleaningProfessional)
                        .map(cleaningProfessionalMapper::toEntity)
                        .collect(Collectors.toCollection(ArrayList::new));

        for (CleaningProfessional cp : cleaningProfessionals) {
            cp.setVehicle(vehicle);
        }

        vehicle.getCleaningProfessionals().addAll(cleaningProfessionals);
        vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }
}
