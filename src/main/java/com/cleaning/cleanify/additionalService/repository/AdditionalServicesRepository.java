package com.cleaning.cleanify.additionalService.repository;

import com.cleaning.cleanify.additionalService.model.AdditionalServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalServicesRepository extends JpaRepository<AdditionalServices, Long> {
}
