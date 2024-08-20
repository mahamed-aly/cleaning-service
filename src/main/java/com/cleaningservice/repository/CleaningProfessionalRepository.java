package com.cleaningservice.repository;

import com.cleaningservice.model.entity.CleaningProfessional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CleaningProfessionalRepository extends JpaRepository<CleaningProfessional, Long> {
}
