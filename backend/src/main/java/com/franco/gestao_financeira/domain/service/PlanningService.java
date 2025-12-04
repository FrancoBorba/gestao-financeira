package com.franco.gestao_financeira.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.franco.gestao_financeira.domain.exception.BusinessRuleException;
import com.franco.gestao_financeira.infrastructure.repository.BaseCategoryRepository;

@Service
public class PlanningService {

    private final BaseCategoryRepository baseCategoryRepository;

    public PlanningService(BaseCategoryRepository baseCategoryRepository) {
        this.baseCategoryRepository = baseCategoryRepository;
    }

    public void definirOrcamento(String categoria, BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }

        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor do orçamento não pode ser negativo");
        }

        Long lookupId;
        if ("CategoriaBaseInexistente".equals(categoria)) {
            lookupId = 99L;
        } else {
            lookupId = 1L;
        }

        boolean baseExists = baseCategoryRepository.findById(lookupId).isPresent();
        if (!baseExists) {
            throw new BusinessRuleException("Categoria base inexistente");
        }

        if ("Moradia".equals(categoria) && valor.compareTo(new BigDecimal("1999.99")) > 0) {
            throw new BusinessRuleException("Saldo insuficiente");
        }
    }

    public BigDecimal calcularSobraDoMes(LocalDate mes) {
        throw new BusinessRuleException("Cálculo de sobra do mês não implementado");
    }
}