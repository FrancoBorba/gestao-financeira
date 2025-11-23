# language: pt
Funcionalidade: Planejamento do Mês (Distribuição de Renda)
  Como um usuário que acabou de receber (seja dia 5, 10 ou 15)
  Eu quero distribuir minha renda nas categorias
  Para garantir que cada centavo tenha um destino antes de eu começar a gastar

  Cenário: Iniciar o mês e definir tetos de gastos
    Dado que eu recebi meu salário e estou iniciando o planejamento de "Novembro"
    E que minhas categorias padrão são "Aluguel" e "Mercado"
    Quando eu importo as categorias para o mês atual
    E defino que o "Aluguel" terá R$ 2000,00
    E defino que o "Mercado" terá R$ 800,00
    Então o meu orçamento total planejado para "Novembro" deve ser de R$ 2800,00
    E a categoria "Aluguel" deve mostrar que tenho R$ 2000,00 disponíveis para gastar



  Cenário: Tentativa de alocar mais do que a renda disponível (Regra de Ouro)
    Dado que minha renda total para "Novembro" é R$ 10000,00
    E que eu já aloquei R$ 8000,00 em outras categorias (Mercado, Aluguel, etc)
    # Sobraram 2000 reais
    Quando eu tento definir o valor de R$ 2500,00 para a categoria "Lazer"
    Então o sistema deve bloquear a operação
    E deve exibir uma mensagem de erro: "Saldo insuficiente. Você só possui R$ 2000,00 livres."
    E o valor planejado da categoria "Lazer" deve permanecer R$ 0,00