![Bootcamp Java DIO](https://img.shields.io/badge/Bootcamp-DIO%20Java-blueviolet?style=for-the-badge&logo=java)

# 🏦DIO BANK🏦


Uma aplicação bancária simples, desenvolvida em Java. Este projeto simula operações bancárias básicas, incluindo gerenciamento de contas, transações e um sistema exclusivo de investimentos.

---

## ✨Funcionalidades✨ 

- **Gerenciamento de Contas**
  - 👤 Criar novas contas com múltiplas chaves PIX.
  - 💰 Depositar fundos em uma conta.
  - 💸 Sacar fundos de uma conta.
  - ↔️ Transferir dinheiro entre contas.
  - 📜 Visualizar histórico detalhado de transações.

- **Sistema de Investimentos**
  - 📈 Criar tipos de investimento com taxas específicas.
  - 📁 Vincular um tipo de investimento a uma conta para criar uma "Carteira de Investimentos".
  - 💹 Investir dinheiro da conta principal na carteira.
  - 🏦 Resgatar fundos da carteira para a conta principal.
  - 🔄 Simular crescimento dos investimentos com base na taxa de rendimento.

- **Listagens e Visualizações**
  - 📄 Listar todas as contas criadas.
  - 📊 Listar todos os tipos de investimento disponíveis.
  - 💼 Listar todas as carteiras de investimento ativas.

---

## 

## Abstrações `Money` e `Wallet`

O saldo de uma conta é representado por uma `List<Money>`, em vez de um tipo numérico simples.

- **`Money`**: Representa uma unidade de moeda (ex.: um centavo), com histórico completo de auditoria (`MoneyAudit`).
- **`Wallet`**: Classe abstrata que gerencia uma lista de `Money`.
  - `AccountWallet`: Conta bancária padrão, identificada por chaves PIX.
  - `InvestmentWallet`: Carteira de investimentos vinculada a uma conta e a um tipo de investimento.

---
## Ultimas Palavras 💖

Esse é o meu primeiro projeto em Java, desenvolvido como parte do Bootcamp de Java oferecido pela DIO (Digital Innovation One). Com o objetivo de colocar em prática tudo o que estava aprendendo — desde os fundamentos da linguagem até conceitos mais avançados como orientação a objetos, modelagem de dados e boas práticas de código.


