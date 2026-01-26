# 🟣 Zaldo - Financial Bot

> **Zaldo** (do latim *Saldo* com um toque moderno) é um Bot de Finanças Pessoais para Telegram de alto desempenho, alimentado por uma API REST robusta.

## 📜 Descrição

Gerencie suas finanças pessoais diretamente do Telegram com o Zaldo! Registre despesas, receitas e visualize seu saldo em tempo real através de comandos simples. A API REST por trás do bot é construída com Spring Boot, garantindo escalabilidade e facilidade de manutenção.

---

## 🚀 Tech Stack & Arquitetura

O projeto foi desenhado para ser agnóstico a infraestrutura e altamente testável.

* **Linguagem:** [Java 25](https://jdk.java.net/25/)
* **Framework:** Spring Boot 4.0.2
* **Arquitetura:** Hexagonal (Ports & Adapters)
* **Banco de Dados:** PostgreSQL 18.1
* **Containerização:** Podman (Rootless) & Docker Compose
* **API Client:** [Bruno](https://www.usebruno.com/) (Coleção inclusa no repo)

### 🏗️ Estrutura do Projeto (Hexagonal)

O código segue rigorosamente a separação de responsabilidades para blindar o domínio:

```text
zaldo-api/
├── core/                  # O Coração (Puro Java, sem Frameworks)
│   ├── domain/            # Entidades (Transaction, User, Wallet)
│   └── ports/             # Interfaces (Entrada/Saída)
├── application/           # Casos de Uso (Services que orquestram o fluxo)
└── infrastructure/        # Detalhes Técnicos (Onde o Spring vive)
    ├── adapters/
    │   ├── in/web/        # Controllers REST
    │   └── out/persistence/ # Repositórios JPA/Hibernate
    └── config/            # Configurações de Beans e Libs
