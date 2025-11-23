# language: pt
Funcionalidade: Gerenciamento de Categorias Padrão
  Como um usuário que quer organizar a vida financeira
  Eu quero definir quais tipos de gastos eu tenho recorrentemente
  Para que eu não precise digitar "Aluguel" todo santo mês

  Cenário: Criar uma nova categoria padrão
    Dado que eu não tenho a categoria "Assinaturas" cadastrada
    Quando eu cadastro uma nova categoria base com nome "Assinaturas" e cor "#FF0000"
    Então essa categoria deve estar disponível para ser usada em meus planejamentos futuros