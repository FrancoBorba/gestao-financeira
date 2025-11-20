package com.franco.gestao_financeira.bdd.steps;

import io.cucumber.java.pt.*;


public class ReserveSteps {

    // @Autowired private ReserveService reserveService;

    @Dado("que existe uma reserva {string} com meta de R$ {double}")
    public void setupReserva(String nome, Double meta) {
        // TODO: repository.save(new Reserve(...))
    }

    @Quando("o usuário {string} adiciona R$ {double} nessa reserva")
    public void adicionarDinheiro(String nomeUsuario, Double valor) {
        // TODO: service.addFundToReserve(...)
    }

    @Então("o saldo atual da reserva deve passar para R$ {double}")
    public void verificarSaldoReserva(Double saldoEsperado) {
        // TODO: Buscar reserva e checar saldo
    }
}