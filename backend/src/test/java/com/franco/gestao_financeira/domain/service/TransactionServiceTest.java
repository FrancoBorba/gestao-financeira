package com.franco.gestao_financeira.domain.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Test
    void deveAtualizarValorGastoAoAdicionarTransacao() {
        // O Básico
        // Cenário: Gasto atual 100. Nova transação 50.
        // Expectativa: Novo gasto da categoria = 150.
    }

    @Test
    void deveEmitirAlertaAmareloAoAtingir80Porcento() {
        // A FEATURE DE NOTIFICAÇÃO
        // Cenário: Teto 1000. Gasto atual 750. Nova compra 60. (Vai para 810)
        // Expectativa: O retorno deve conter um status ou flag "WARNING_THRESHOLD_REACHED"
    }

    @Test
    void deveEmitirAlertaVermelhoAoEstourarOrcamento() {
        // Cenário: Teto 100. Gasto atual 90. Nova compra 20. (Vai para 110)
        // Expectativa: A transação passa (gasto real), mas retorna flag "BUDGET_OVERFLOW".
    }

    @Test
    void deveRejeitarTransacaoSemValor() {
        // Cenário: Transação com valor 0 ou Null.
        // Expectativa: Exception.
    }
}
