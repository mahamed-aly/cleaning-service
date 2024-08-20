package com.cleaningservice.controller;

import com.cleaningservice.model.request.CreateVehicleRequest;
import com.cleaningservice.model.dto.VehicleDTO;
import com.cleaningservice.model.entity.Vehicle;
import com.cleaningservice.service.VehicleService;
import com.cleaningservice.util.PaginationUtil;
import com.cleaningservice.util.UriUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@AllArgsConstructor
@Slf4j
public class VehicleController {
    private final VehicleService vehicleService;

    /**
     * GET : get a specific vehicle by ID
     *
     * @param id vehicle ID
     * @return http status ok (200) and the required vehicle details in the body
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        log.debug("Rest request to get a vehicle by id: {}", id);
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    /**
     * GET : get all available vehicles
     *
     * @return the ResponseEntity with http status ok (200) and all available vehicles in the body
     */
    @GetMapping
    @Operation(summary = "Get all vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles(@RequestParam(defaultValue = "1") int pageNumber,
                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(pageNumber - 1, size);
        log.info("Rest request to get all vehicles");
        final Page<Vehicle> page = vehicleService.getAllPaginated(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/vehicles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST : create new vehicle
     *
     * @return the ResponseEntity with http status created (201) and the created vehicle in the body
     */
    @Operation(summary = "Create vehicle")
    @PostMapping
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody CreateVehicleRequest createVehicleRequest) {
        log.info("Rest request to create a new vehicle");
        VehicleDTO response = vehicleService.createVehicle(createVehicleRequest);
        return ResponseEntity.created(UriUtil.locationUri(response.getId().toString())).body(response);
    }

    /**
     * PATCH : assign cleaning professionals to vehicle
     *
     * @param id                       vehicle ID
     * @param cleaningProfessionalsIds cleaning professionals IDs
     * @return the ResponseEntity with http status ok (200)
     */
    @Operation(summary = "Assign cleaning professional to vehicle")
    @PatchMapping("/add-cleaning-professionals/{id}")
    public ResponseEntity<VehicleDTO> assignCleanersToVehicle(@PathVariable Long id,
                                                              @RequestBody List<Long> cleaningProfessionalsIds) {
        log.info("Rest request to assign cleaning professionals IDs {} to vehicle ID: {}", cleaningProfessionalsIds, id);
        return ResponseEntity.ok(vehicleService.assignCleaningProfessionalsToVehicle(id, cleaningProfessionalsIds));
    }
}
