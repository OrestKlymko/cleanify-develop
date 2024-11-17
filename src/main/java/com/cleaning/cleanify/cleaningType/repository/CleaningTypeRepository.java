package com.cleaning.cleanify.cleaningType.repository;

import com.cleaning.cleanify.cleaningType.model.CleaningType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CleaningTypeRepository extends JpaRepository<CleaningType, Long> {
}
