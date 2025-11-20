# language: pt
Funcionalidade: Alertas de Consumo de Orçamento
  Como um usuário distraído
  Eu quero ser avisado quando estiver perto de estourar o teto de uma categoria
  Para que eu possa frear meus gastos antes que seja tarde

  Cenário: Atingimento de zona de alerta (80%)
    Dado que a categoria "Restaurantes" tem um teto de R$ 500,00
    E que o total gasto atual é de R$ 350,00
    Quando eu registro uma nova despesa de R$ 50,00 (Totalizando R$ 400,00)
    Então a transação deve ser aceita
    Mas o sistema deve emitir um ALERTA AMARELO: "Cuidado! Você já consumiu 80% do seu orçamento de Restaurantes."

  Cenário: Estouro de orçamento
    Dado que a categoria "Lazer" tem teto de R$ 100,00 e já gastei R$ 90,00
    Quando eu tento registrar um gasto de R$ 20,00
    Então o sistema deve permitir (pois o gasto já ocorreu na vida real)
    Mas deve emitir um ALERTA VERMELHO: "Orçamento Estourado em R$ 10,00!"