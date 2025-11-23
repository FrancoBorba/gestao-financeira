# language: pt
Funcionalidade: Categorias Sazonais (Exceções do Mês)
  Como um usuário que tem gastos variáveis
  Eu quero adicionar categorias específicas apenas para o mês atual
  Para cobrir eventos como Natal, Aniversários ou Viagens curtas

  Cenário: Adicionar gastos de Natal em Dezembro
    Dado que eu já iniciei o planejamento de "Dezembro/2025"
    Mas eu não tenho uma categoria base "Presentes de Natal"
    Quando eu adiciono uma categoria extra "Natal" com valor de R$ 1000,00 apenas para este mês
    Então essa categoria deve aparecer no meu gráfico de Dezembro
    Mas ela NÃO deve aparecer automaticamente quando eu for planejar Janeiro (pois é sazonal)