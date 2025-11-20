# language: pt
Funcionalidade: Ciclo Financeiro Personalizado
  Como um usuário que recebe salário no meio do mês
  Eu quero que meu planejamento siga meu ciclo de recebimento (ex: dia 15 a 15)
  Para que meus relatórios reflitam minha realidade financeira

  Cenário: Planejamento com ciclo iniciando dia 15
    Dado que o usuário configurou o "Dia de Início de Ciclo" para o dia 15
    E que hoje é dia "20 de Novembro"
    Quando o usuário inicia um novo planejamento
    Então o sistema deve criar o orçamento com referência "Novembro"
    E o período de vigência desse orçamento deve ser de "15/11" até "14/12"