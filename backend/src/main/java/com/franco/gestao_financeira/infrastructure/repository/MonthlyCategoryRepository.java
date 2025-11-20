package com.franco.gestao_financeira.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.franco.gestao_financeira.domain.model.MonthlyCategory;

@Repository
public interface MonthlyCategoryRepository extends JpaRepository<MonthlyCategory, Long> {
    
    // Search Category by a especif month
    @Query("SELECT mc FROM MonthlyCategory mc WHERE mc.referenceMonth = :month")
    List<MonthlyCategory> findByMonth(@Param("month") LocalDate referenceMonth);

    Optional<MonthlyCategory> findByBaseCategoryIdAndReferenceMonth(Long baseCategoryId, LocalDate referenceMonth);
}