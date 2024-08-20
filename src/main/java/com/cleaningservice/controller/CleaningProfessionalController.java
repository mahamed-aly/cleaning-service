package com.cleaningservice.controller;

import com.cleaningservice.model.CleaningProfessionalAvailability;
import com.cleaningservice.model.request.CreateCleaningProfessionalRequest;
import com.cleaningservice.model.request.GetAvailableCleaningProfessionalsRequest;
import com.cleaningservice.model.dto.CleaningProfessionalDTO;
import com.cleaningservice.service.CleaningProfessionalService;
import com.cleaningservice.util.UriUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cleaning-professionals")
@AllArgsConstructor
@Slf4j
public class CleaningProfessionalController {

    private final CleaningProfessionalService cleaningProfessionalService;

    /**
     * GET : get a specific cleaning professional by ID
     *
     * @param id customer ID
     * @return http status ok (200) and the required cleaning professional details in the body
     */
    @Operation(summary = "Get cleaning professional by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CleaningProfessionalDTO> getCleaningProfessional(@PathVariable Long id) {
        return ResponseEntity.ok(cleaningProfessionalService.getCleaningProfessional(id));
    }

    /**
     * GET : get a list of all available cleaning professional
     *
     * @return http status ok (200) and available cleaning professionals in the body
     */
    @Operation(summary = "Get a list of all available cleaning professional")
    @GetMapping("/available-cleaning-professionals")
    public ResponseEntity<List<List<CleaningProfessionalAvailability>>> getAvailableCleaningProfessionals
    (@Valid @RequestBody GetAvailableCleaningProfessionalsRequest getAvailableCleaningProfessionalsRequest) {
        log.info("Rest request to get available cleaning professionals on : {}", getAvailableCleaningProfessionalsRequest.date());
        return ResponseEntity.ok(cleaningProfessionalService.getAvailableCleaningProfessionals(getAvailableCleaningProfessionalsRequest));
    }

    /**
     * POST : create new cleaning professional
     *
     * @return the ResponseEntity with http status created (201) and the created cleaning professional in the body
     */
    @Operation(summary = "Create cleaning professional")
    @PostMapping
    public ResponseEntity<CleaningProfessionalDTO> createCleaningProfessional
    (@Valid @RequestBody CreateCleaningProfessionalRequest createCleaningProfessionalRequest) {
        log.info("Rest request to create a cleaning professional");
        CleaningProfessionalDTO response = cleaningProfessionalService.createCleaningProfessional(createCleaningProfessionalRequest);
        return ResponseEntity.created(UriUtil.locationUri(response.getId().toString())).body(response);
    }
}
