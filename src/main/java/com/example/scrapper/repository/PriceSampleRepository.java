package com.example.scrapper.repository;

import com.example.scrapper.model.entity.PriceSampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PriceSampleRepository extends JpaRepository<PriceSampleEntity, UUID> {
    List<PriceSampleEntity> findByStartDateGreaterThanEqualAndEndDateLessThanEqual(LocalDate startDate, LocalDate endDate);
    @Query("select p from PriceSampleEntity p where p.startDate <= ?1 and p.endDate >= ?1")
    Optional<PriceSampleEntity> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate);
}
