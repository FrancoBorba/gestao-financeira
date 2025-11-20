package com.franco.gestao_financeira.domain.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PlanningServiceTest {
    

    @Test
void naoDevePermitirOrcamentoNegativo() {
    // Cenário: Tentar definir -500,00 para "Lazer"
    // Expectativa: Lançar IllegalArgumentException
}

@Test
void deveBloquearOrcamentoSeExcederRendaDisponivel() {
    // A REGRA DE OURO
    // Cenário: Renda 10.000. Já alocado 9.000. Tenta alocar 2.000.
    // Expectativa: Lançar SaldoInsuficienteException
}

@Test
void deveCalcularSobraDoMesCorretamente() {
    // Cenário: Renda 5.000. Categorias somam 3.500.
    // Expectativa: Retornar 1.500
}

@Test
void deveImpedirCriacaoDeCategoriaMensalSemCategoriaBase() {
    // Cenário: Tentar planejar um mês para uma categoria que não existe na base.
    // Expectativa: Erro de integridade.
}
}
