package com.franco.gestao_financeira.domain.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.franco.gestao_financeira.domain.exception.BusinessRuleException;
import com.franco.gestao_financeira.infrastructure.repository.BaseCategoryRepository;
import com.franco.gestao_financeira.domain.model.BaseCategory;

@ExtendWith(MockitoExtension.class)
public class PlanningServiceTest {

    @Mock
    BaseCategoryRepository baseCategoryRepository;

    @InjectMocks
    PlanningService planningService;

    BaseCategory baseCategory;

    @BeforeEach
    void setUp() {
        baseCategory = new BaseCategory();
        baseCategory.setId(1L);
        baseCategory.setName("Lazer");
    }

    @Test
    void naoDevePermitirOrcamentoNegativo() {

        // Arrange
        String categoria = "Lazer";
        BigDecimal valorNegativo = new BigDecimal("-500.00");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            planningService.definirOrcamento(categoria, valorNegativo);
        });
    }

    @Test
    void deveBloquearOrcamentoSeExcederRendaDisponivel() {
        String categoria = "Moradia";
        BigDecimal valorTentativa = new BigDecimal("2000.00");

        // Arrange
        when(baseCategoryRepository.findById(1L)).thenReturn(Optional.of(baseCategory));

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> {
            planningService.definirOrcamento(categoria, valorTentativa);
        });
    }

    @Test
    void deveCalcularSobraDoMesCorretamente() {
        LocalDate mes = LocalDate.of(2025, 11, 1);

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> {
            planningService.calcularSobraDoMes(mes);
        });
    }

    @Test
    void deveImpedirCriacaoDeCategoriaMensalSemCategoriaBase() {
        String categoriaInexistente = "CategoriaBaseInexistente";
        BigDecimal valor = new BigDecimal("100.00");

        when(baseCategoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BusinessRuleException.class, () -> {
            planningService.definirOrcamento(categoriaInexistente, valor);
        });
    }
}
