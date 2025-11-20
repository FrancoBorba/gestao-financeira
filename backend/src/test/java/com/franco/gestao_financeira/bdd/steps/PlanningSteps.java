package com.franco.gestao_financeira.bdd.steps;

import io.cucumber.java.pt.*;
import org.junit.jupiter.api.Assertions;

public class PlanningSteps {

    // injetar quando for implementar
    // @Autowired private PlanningService planningService;
    // @Autowired private BaseCategoryRepository baseCategoryRepository;

    private Long userId = 1L;
    
    private Exception exceptionCapturada; // Para testar os erros

    // --- FEATURE 1: BASE ---
    @Dado("que eu não tenho a categoria {string} cadastrada")
    public void garantirCategoriaInexistente(String nome) {
        // TODO: Deletar do banco se existir
    }

    @Quando("eu cadastro uma nova categoria base com nome {string} e cor {string}")
    public void cadastrarCategoriaBase(String nome, String cor) {
        // TODO: Chamar repository.save(new BaseCategory(...))
    }

    // --- FEATURE 2, 3 e 4: PLANEJAMENTO ---
    @Dado("que minhas categorias padrão são {string} e {string}")
    public void setupCategoriasPadrao(String cat1, String cat2) {
        // TODO: Criar e salvar categorias base
    }
    
    @Dado("que o usuário configurou o {string} para o dia {int}")
    public void configurarCiclo(String config, Integer dia) {
        // TODO: Atualizar o User com o dia do ciclo
    }

    @Quando("eu inicio o planejamento para {string}")
    public void iniciarPlanejamento(String mesAno) {
        // TODO: Chamar service.startMonthPlanning(...)
    }
    
    @Quando("eu adiciono uma categoria extra {string} com valor de R$ {double}")
    public void adicionarSazonal(String nome, Double valor) {
        // TODO: Chamar service.addSeasonalCategory(...)
    }

    @Quando("tento definir o valor de R$ {double} para a categoria {string}")
    public void tentarDefinirOrcamento(Double valor, String categoria) {
        try {
            // TODO: Chamar service.updateBudget(...)
        } catch (Exception e) {
            this.exceptionCapturada = e; // Captura o erro para validar no passo "Então"
        }
    }

    @Então("o sistema deve bloquear a operação")
    public void verificarBloqueio() {
        Assertions.assertNotNull(exceptionCapturada, "Deveria ter dado erro, mas não deu!");
    }
}