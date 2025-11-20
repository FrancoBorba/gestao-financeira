package com.franco.gestao_financeira.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "monthly_category")
public class MonthlyCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_category_id", nullable = false)
    private BaseCategory baseCategory;

    @Column(name = "reference_month", nullable = false)
    private LocalDate referenceMonth;

    @Column(name = "planned_amount", nullable = false)
    private BigDecimal plannedAmount = BigDecimal.ZERO;

    @Column(name = "amount_spent")
    private BigDecimal amountSpent = BigDecimal.ZERO;
}
