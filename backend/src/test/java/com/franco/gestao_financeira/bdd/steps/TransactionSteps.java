package com.franco.gestao_financeira.bdd.steps;

import io.cucumber.java.pt.*;


public class TransactionSteps {

    // @Autowired private TransactionService transactionService;
    
    private String alertaRecebido = null; // Para guardar o alerta do sistema

    @Dado("que a categoria {string} tem um teto de R$ {double}")
    public void setupCategoriaComTeto(String categoria, Double teto) {
        // TODO: Criar categoria mensal com esse valor planejado
    }
    
    @Dado("que o total gasto atual é de R$ {double}")
    public void setupGastoAtual(Double gasto) {
        // TODO: Inserir uma transação inicial para atingir esse valor
    }

    @Quando("eu registro uma nova despesa de R$ {double} na categoria {string}")
    public void registrarDespesa(Double valor, String categoria) {
        // TODO: TransactionResult result = service.addTransaction(...)
        // alertaRecebido = result.getWarningMessage();
    }

    @Então("a transação deve ser aceita")
    public void transacaoAceita() {
        // TODO: Verificar se salvou no banco
    }

    @Então("o sistema deve emitir um ALERTA {string}: {string}")
    public void verificarAlerta(String tipoAlerta, String mensagem) {
        // TODO: Assertions.assertEquals(mensagem, alertaRecebido);
    }
}