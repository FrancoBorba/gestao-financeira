# language: pt
Funcionalidade: Destinação Inteligente de Sobras (Fechamento do Mês)
  Como um usuário que quer enriquecer
  Eu quero que o sistema identifique dinheiro não gasto no mês anterior
  E sugira mover para uma reserva

  Cenário: Fechamento de mês com saldo positivo
    Dado que o mês de "Novembro" encerrou
    E que a categoria "Mercado" tinha R$ 1000,00 planejados mas só gastei R$ 800,00
    Quando eu abro o planejamento de "Dezembro"
    Então o sistema deve me notificar: "Você economizou R$ 200,00 em Novembro."
    E deve perguntar: "Deseja transferir essa sobra para a reserva 'Viagem'?"