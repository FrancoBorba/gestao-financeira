# language: pt
@planning
Funcionalidade: Gerenciamento de Categorias Padrão
  Como um usuário que quer organizar a vida financeira
  Eu quero definir quais tipos de gastos eu tenho recorrentemente
  Para que eu não precise digitar "Aluguel" todo santo mês

  Contexto:
    Dado que eu sou um usuário cadastrado com nome "Franco"

  Cenário: Criar uma nova categoria padrão com sucesso
    Dado que eu não tenho a categoria "Assinaturas" cadastrada
    Quando eu cadastro uma nova categoria base com nome "Assinaturas" e cor "#FF0000"
    Então essa categoria deve estar disponível para ser usada


  Cenário: Tentar cadastrar categoria com nome duplicado
    Dado que eu já possuo a categoria "Lazer" cadastrada
    Quando eu tento cadastrar uma nova categoria com nome "Lazer" e cor "#00FF00"
    Então o sistema deve bloquear a operação de categoria
    E deve exibir a mensagem de erro de planejamento: "Categoria já existe: Lazer"

  Cenário: Tentar cadastrar categoria com cor inválida
    Dado que eu não tenho a categoria "Viagem" cadastrada
    Quando eu tento cadastrar uma nova categoria com nome "Viagem" e cor "Azul-Marinho"
    Então o sistema deve bloquear a operação de categoria
    E deve exibir a mensagem de erro de planejamento: "Formato de cor inválido"