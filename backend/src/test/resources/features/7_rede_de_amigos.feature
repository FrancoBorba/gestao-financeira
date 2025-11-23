# language: pt
@social
Funcionalidade: Rede de Amigos e Conexões
  Como um usuário que quer compartilhar metas
  Eu quero adicionar amigos na plataforma
  Para criar reservas conjuntas no futuro

  Contexto:
    Dado que existem os usuários "Franco" e "Maria" cadastrados

  Cenário: Enviar pedido de amizade com sucesso
    Dado que "Franco" e "Maria" não possuem vínculo
    Quando "Franco" envia um pedido de amizade para "Maria"
    Então o sistema deve registrar uma nova amizade
    E o status da amizade deve ser "PENDING"

  Cenário: Tentar adicionar usuário que já é amigo (Erro)
    Dado que "Franco" e "Maria" já são amigos com status "ACCEPTED"
    Quando "Franco" tenta enviar um pedido de amizade para "Maria"
    Então o sistema deve bloquear a solicitação de amizade
    E deve exibir a mensagem de erro: "You already friend"

  Cenário: Aceitar um pedido pendente
    Dado que existe uma solicitação de "Franco" para "Maria" com status "PENDING"
    Quando "Maria" aceita a solicitação
    Então o status da amizade deve mudar para "ACCEPTED"