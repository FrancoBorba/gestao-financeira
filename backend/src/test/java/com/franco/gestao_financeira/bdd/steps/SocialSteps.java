package com.franco.gestao_financeira.bdd.steps;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import com.franco.gestao_financeira.domain.model.Friendship;
import com.franco.gestao_financeira.domain.model.User;
import com.franco.gestao_financeira.domain.model.Enums.FriendshipStatus;
import com.franco.gestao_financeira.domain.service.FriendshipService;
import com.franco.gestao_financeira.infrastructure.repository.FriendshipRepository;
import com.franco.gestao_financeira.infrastructure.repository.UserRepository;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class SocialSteps {

    @Autowired private FriendshipService friendshipService;
    @Autowired private UserRepository userRepository;
    @Autowired private FriendshipRepository friendshipRepository;

    private Exception exceptionCapturada;
    private User franco;
    private User maria;

    // --- CONTEXTO (PREPARAÇÃO DO BANCO) ---

    @Dado("que existem os usuários {string} e {string} cadastrados")
    public void criarUsuariosIniciais(String nome1, String nome2) {
        // Limpar banco para evitar dados de testes anteriores
        friendshipRepository.deleteAll();
        userRepository.deleteAll();

        // 1. Criando o FRANCO usando o seu construtor:
        // (ID null, Name, Email, Nickname, CycleDay)
        franco = new User(
            null, 
            nome1, 
            nome1.toLowerCase() + "@email.com", 
            nome1.toLowerCase() + ".dev", // Nickname: franco.dev
            1
        );
        franco = userRepository.save(franco);

        // 2. Criando a MARIA usando o seu construtor:
        maria = new User(
            null, 
            nome2, 
            nome2.toLowerCase() + "@email.com", 
            nome2.toLowerCase() + ".fin", // Nickname: maria.fin
            1
        );
        maria = userRepository.save(maria);
    }

    @Dado("que {string} e {string} não possuem vínculo")
    public void garantirSemVinculo(String u1, String u2) {
        Assertions.assertEquals(0, friendshipRepository.count());
    }
    
    @Dado("que {string} e {string} já são amigos com status {string}")
    public void criarAmizadeExistente(String u1, String u2, String statusStr) {
        Friendship f = new Friendship();
        f.setRequester(franco);
        f.setReceiver(maria);
        f.setStatusFriendship(FriendshipStatus.valueOf(statusStr)); 
        friendshipRepository.save(f);
    }

     @Dado("que existe uma solicitação de {string} para {string} com status {string}")
    public void criarSolicitacaoExistente(String u1, String u2, String statusStr) {
        // Lógica idêntica ao "criarAmizadeExistente", mas com a frase nova
        Friendship f = new Friendship();
        f.setRequester(franco);
        f.setReceiver(maria);
        f.setStatusFriendship(FriendshipStatus.valueOf(statusStr)); 
        friendshipRepository.save(f);
    }


    // --- AÇÃO (CHAMADA DO SERVICE) ---

    @Quando("{string} envia um pedido de amizade para {string}")
    public void enviarPedido(String nomeRemetente, String nomeDestinatario) {
        try {
       
            // Passamos o ID do Franco e o NICKNAME da Maria
            // O Gherkin diz "Maria", mas o sistema usa "maria.fin" que criamos no setup
            
            if (nomeRemetente.equalsIgnoreCase(franco.getName())) {
                friendshipService.sendFriendRequest(franco.getId(), maria.getNickname());
            } else {
                // Caso inverso (se precisarmos testar Maria pedindo pra Franco)
                friendshipService.sendFriendRequest(maria.getId(), franco.getNickname());
            }
            
        } catch (Exception e) {
            this.exceptionCapturada = e;
        }
    }
    
    @Quando("{string} tenta enviar um pedido de amizade para {string}")
    public void tentarEnviarPedido(String u1, String u2) {
        enviarPedido(u1, u2); // Reutiliza a lógica acima
    }
    
    @Quando("{string} aceita a solicitação")
    public void aceitarSolicitacao(String nomeUsuario) {
        // Busca a solicitação pendente no banco para ter o ID
        Optional<Friendship> solicitacao = friendshipRepository.findByRequesterAndReceiver(franco, maria);
        if(solicitacao.isPresent()) {
            friendshipService.acceptFriendRequest(solicitacao.get().getId());
        }
    }

    // --- VERIFICAÇÃO (ASSERTS) ---

    @Então("o sistema deve registrar uma nova amizade")
    public void verificarRegistroCriado() {
        Assertions.assertEquals(1, friendshipRepository.count());
    }

    @Então("o status da amizade deve ser {string}")
    public void verificarStatus(String statusEsperado) {
        Friendship f = friendshipRepository.findAll().get(0);
        Assertions.assertEquals(FriendshipStatus.valueOf(statusEsperado), f.getStatusFriendship());
    }

    @Então("o sistema deve bloquear a solicitação de amizade")
    public void verificarBloqueio() {
        Assertions.assertNotNull(exceptionCapturada, "Erro esperado não ocorreu!");
    }

    @Então("deve exibir a mensagem de erro: {string}")
    public void verificarMensagemErro(String msgEsperada) {
        // Verifica se a mensagem bate, ignorando maiúsculas/minúsculas
        Assertions.assertTrue(exceptionCapturada.getMessage().toLowerCase().contains(msgEsperada.toLowerCase()),
                "Mensagem incorreta! Recebida: " + exceptionCapturada.getMessage());
    }

 
    @Então("o status da amizade deve mudar para {string}")
    public void verificarMudancaDeStatus(String statusEsperado) {
        // Lógica idêntica ao "verificarStatus", mas com a frase nova
        Friendship f = friendshipRepository.findAll().get(0);
        Assertions.assertEquals(FriendshipStatus.valueOf(statusEsperado), f.getStatusFriendship());
    }
}