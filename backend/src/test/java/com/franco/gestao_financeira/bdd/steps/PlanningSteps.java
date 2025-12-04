package com.franco.gestao_financeira.bdd.steps;

import com.franco.gestao_financeira.application.dto.CategoryDTO;
import com.franco.gestao_financeira.domain.model.User;
import com.franco.gestao_financeira.domain.service.CategoryService;
import com.franco.gestao_financeira.infrastructure.repository.BaseCategoryRepository;
import com.franco.gestao_financeira.infrastructure.repository.UserRepository;
import io.cucumber.java.pt.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

public class PlanningSteps {

    @Autowired private CategoryService categoryService;
    @Autowired private UserRepository userRepository;
    @Autowired private BaseCategoryRepository baseCategoryRepository;

    private User usuarioLogado;
    private Exception exceptionCapturada;

    // ========================================================================
    // CONTEXTO (PREPARAÇÃO)
    // ========================================================================

    @Dado("que eu sou um usuário cadastrado com nome {string}")
    public void setupUsuario(String nome) {
        // Limpa o banco para garantir teste isolado
        baseCategoryRepository.deleteAll();
        userRepository.deleteAll();

        // Cria o usuário real no H2
        usuarioLogado = new User(null, nome, "teste@email.com", nome.toLowerCase() + ".dev", 1);
        usuarioLogado = userRepository.save(usuarioLogado);
    }

  

    @Dado("que eu não tenho a categoria {string} cadastrada")
    public void garantirInexistencia(String nomeCategoria) {
        boolean existe = baseCategoryRepository.existsByNameAndUser(nomeCategoria, usuarioLogado);
        Assertions.assertFalse(existe, "A categoria não deveria existir antes do teste!");
    }

    @Quando("eu cadastro uma nova categoria base com nome {string} e cor {string}")
    public void cadastrarCategoria(String nome, String cor) {
        try {
            CategoryDTO dto = new CategoryDTO(nome, cor);
            // Chama o Service Real de Categoria
            categoryService.createCategory(usuarioLogado.getId(), dto);
        } catch (Exception e) {
            this.exceptionCapturada = e; // Captura erro se houver
        }
    }

    @Então("essa categoria deve estar disponível para ser usada")
    public void verificarCategoriaSalva() {
        // Verifica se salvou 1 registro
        Assertions.assertEquals(1, baseCategoryRepository.count());
        // Garante que não deu erro
        Assertions.assertNull(exceptionCapturada, "Não deveria ter dado erro!");
    }


    @Dado("que eu já possuo a categoria {string} cadastrada")
    public void setupCategoriaExistente(String nome) {
        // Pré-condição: Criamos a categoria usando o SERVICE para simular que já existe
        try {
            CategoryDTO dto = new CategoryDTO(nome, "#FFFFFF");
            categoryService.createCategory(usuarioLogado.getId(), dto);
        } catch (Exception e) {
            Assertions.fail("Erro ao preparar cenário: " + e.getMessage());
        }
    }

    @Quando("eu tento cadastrar uma nova categoria com nome {string} e cor {string}")
    public void tentarCadastrarCategoria(String nome, String cor) {
        // Reutiliza o método de cadastro que já tem o bloco try/catch
        cadastrarCategoria(nome, cor);
    }

    @Então("o sistema deve bloquear a operação de categoria")
    public void verificarBloqueioCategoria() {
        Assertions.assertNotNull(exceptionCapturada, "O sistema deveria ter bloqueado, mas deixou passar!");
    }

    @Então("deve exibir a mensagem de erro de planejamento: {string}")
    public void verificarMensagemErroPlanejamento(String msgEsperada) {
        String msgReal = exceptionCapturada.getMessage();
        
        Assertions.assertTrue(msgReal.toLowerCase().contains(msgEsperada.toLowerCase()),
                "Mensagem incorreta! Esperava conter: '" + msgEsperada + "' mas veio: '" + msgReal + "'");
    }  
}