# PolarisBank 

O **PolarisBank** é um projeto de backend desenvolvido em Java com Spring Boot, cujo objetivo é implementar o núcleo de operações de um sistema bancário, contemplando funcionalidades como gerenciamento de clientes, contas e movimentações financeiras.

O projeto adota uma arquitetura de **monólito modular**, com separação de responsabilidades por domínio, buscando organização, manutenibilidade e consistência das operações financeiras.

O desenvolvimento tem como foco a aplicação de conceitos de engenharia de software, modelagem de domínio, desenvolvimento de APIs REST, persistência de dados relacionais, testes automatizados e boas práticas de desenvolvimento backend.

## Fases de desenvolvimento

O desenvolvimento do PolarisBank está dividido em três fases, com evolução progressiva das funcionalidades e da arquitetura.

### Fase 1 — Construção do núcleo financeiro

- Desenvolvimento de uma API REST utilizando Java e Spring Boot.
- Implementação da persistência de dados com JPA e banco de dados SQL.
- Construção das funcionalidades fundamentais do núcleo financeiro.
- Estruturação do projeto como monólito modular.

### Fase 2 — Segurança e confiabilidade

- Implementação de autenticação e mecanismos de segurança.
- Containerização da aplicação com Docker.
- Implementação de mecanismos para lidar com concorrência.
- Evolução das operações financeiras, priorizando confiabilidade e integridade dos dados.

### Fase 3 — Arquitetura de microsserviços

- Evolução dos módulos do monólito para serviços independentes.
- Implementação de comunicação e integração entre serviços.
- Introdução de mensageria.
- Preservação das regras de negócio e da integridade financeira na arquitetura distribuída.

## Metodologia de desenvolvimento

O projeto adota **Test-Driven Development (TDD)**, aplicando o ciclo Red-Green-Refactor durante o desenvolvimento das funcionalidades.

Os testes automatizados são desenvolvidos continuamente em todas as fases, acompanhando a implementação e a evolução das regras de negócio.

## Arquitetura

O PolarisBank adota inicialmente uma arquitetura de **monólito modular**, organizando a aplicação em módulos independentes por domínio de negócio.

Essa abordagem permite manter a simplicidade operacional de uma aplicação única, enquanto promove a separação de responsabilidades e facilita a manutenção e a evolução do sistema.

### Organização da aplicação

A aplicação utiliza uma arquitetura em camadas para separar as responsabilidades:

- **Controller:** recebe as requisições HTTP e disponibiliza os endpoints da API REST.
- **Service:** concentra a lógica de negócio e coordena as operações.
- **Repository:** responsável pelo acesso e pela persistência dos dados.
- **DTOs:** objetos utilizados para transportar dados entre as diferentes partes da aplicação e na comunicação com os consumidores da API.

### Diretrizes arquiteturais

- Organização dos módulos por domínio de negócio.
- Separação de responsabilidades entre as camadas.
- Preservação da integridade e da consistência das operações financeiras.
- Evitar abstrações e complexidades que não tragam benefícios proporcionais.

A arquitetura poderá evoluir para microsserviços na terceira fase do projeto.

## Módulos do sistema

O PolarisBank está organizado em módulos que representam diferentes responsabilidades do domínio bancário.

### Customer

Responsável pelo gerenciamento dos clientes pessoa física, incluindo cadastro, consulta e controle de status.

**Status:** Em desenvolvimento.

### Account

Responsável pelo gerenciamento das contas bancárias, incluindo abertura, consulta e controle de status.

**Status:** Planejado.

### Ledger

Responsável pelo registro e controle das movimentações financeiras, utilizando o conceito de partidas dobradas (*Double-Entry Bookkeeping*) para manter a consistência dos registros contábeis.

O módulo contempla as estruturas de contas contábeis, lançamentos e transações financeiras.

**Status:** Planejado.

## Tecnologias utilizadas

### Linguagem

- Java 21

### Framework e desenvolvimento

- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Hibernate ORM
- Bean Validation

### Banco de dados e migrações

- PostgreSQL
- Flyway

### Documentação da API - Pendente

- SpringDoc OpenAPI
- Swagger UI

### Configuração

- dotenv-java

### Testes automatizados

- JUnit 5
- Mockito
- Spring Boot Test
- Testcontainers
- PostgreSQL Testcontainers

### Build e gerenciamento de dependências

- Gradle

## Modelagem e regras de negócio

A modelagem do PolarisBank busca representar as regras e os conceitos do domínio bancário, mantendo uma única fonte de verdade para cada informação de negócio.

As decisões de modelagem seguem os seguintes princípios:

- O modelo deve representar a realidade do negócio, e não apenas as conveniências da implementação.
- Cada entidade deve possuir responsabilidades bem definidas.
- As regras de negócio devem ser centralizadas e preservadas dentro de seus respectivos domínios.
- As decisões arquiteturais devem trazer benefícios proporcionais à complexidade adicionada.

### Principais regras de negócio

**Customer**

- CPF deve ser único e válido.
- Nome é obrigatório.
- E-mail deve ser único.
- Data de nascimento não pode ser futura.
- O cliente possui um status: `ACTIVE`, `INACTIVE` ou `BLOCKED`.
- Clientes não são removidos fisicamente do sistema.

**Account**

- Cada conta pertence a um cliente.
- O número da conta deve ser único.
- Toda conta é criada com status `ACTIVE`.
- Somente contas `ACTIVE` podem realizar transações.

**Ledger**

- Os registros financeiros seguem o princípio de partidas dobradas (*Double-Entry Bookkeeping*), garantindo o equilíbrio dos lançamentos contábeis.

## Estratégia de testes

O PolarisBank utiliza **Test-Driven Development (TDD)** como metodologia de desenvolvimento.

A estratégia contempla:

- **Testes unitários:** validam comportamentos e regras de negócio de forma isolada, utilizando mocks quando necessário.
- **Testes de integração:** verificam a integração entre componentes da aplicação, incluindo persistência e acesso ao banco de dados.
- **Testes de API:** validam os endpoints HTTP, incluindo requisições, respostas e regras de validação.

### Ferramentas utilizadas

- JUnit
- Mockito
- Testcontainers
- PostgreSQL

Os testes são desenvolvidos continuamente em todas as fases do projeto.

## Como executar o projeto

### Pré-requisitos

- **Java 21 (JDK):** ambiente de desenvolvimento e execução.
- **PostgreSQL:** banco de dados relacional.
- **Git:** controle de versão.

O projeto utiliza **Spring Boot 4.1.1** e **Gradle** para gerenciamento de dependências e build.

### 1. Clonar o repositório

```bash
git clone <URL_DO_REPOSITORIO>
cd <DIRETORIO_DO_PROJETO>
```

### 2. Criar o banco de dados

No PostgreSQL, execute:

```sql
CREATE DATABASE polaris_bank;
```

### 3. Configurar as variáveis de ambiente

Na raiz do projeto, crie um arquivo `.env` com as seguintes variáveis:

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/polaris_bank
DATABASE_USERNAME=seu_usuario
DATABASE_PASSWORD=sua_senha
```

Substitua os valores pelas credenciais e configurações do seu ambiente.

### 4. Executar a aplicação

**Importante:** os comandos abaixo devem ser executados no terminal do sistema operacional (Terminal do IntelliJ IDEA, VS Code, PowerShell, CMD ou terminal Linux), estando na raiz do projeto, onde se encontra o arquivo `gradlew` ou `gradlew.bat`.

Não execute esses comandos no console SQL do PostgreSQL.

**Windows (PowerShell ou CMD):**

```powershell
.\gradlew.bat bootRun
```

**Linux:**

```bash
./gradlew bootRun
```

Esse comando compila e inicia a aplicação Spring Boot.

As migrations do Flyway são executadas durante a inicialização da aplicação.

### 5. Executar os testes

Abra um terminal na raiz do projeto. Os testes podem ser executados com a aplicação em funcionamento ou interrompida, desde que os serviços necessários aos testes estejam disponíveis.

**Windows (PowerShell ou CMD):**

```powershell
.\gradlew.bat test
```

**Linux:**

```bash
./gradlew test
```

O Gradle executará os testes automatizados e apresentará o resultado no terminal.

Os relatórios de execução dos testes ficam disponíveis em:

```text
build/reports/tests/test/index.html
```

## Roadmap

### Fase 1 — Construção do núcleo financeiro

- [ ] Implementação do módulo Customer.
- [ ] Implementação do módulo Account.
- [ ] Implementação do módulo Ledger.
- [ ] Desenvolvimento das operações financeiras fundamentais.

### Fase 2 — Segurança e confiabilidade

- [ ] Implementação de autenticação e mecanismos de segurança.
- [ ] Containerização da aplicação com Docker.
- [ ] Implementação de mecanismos de controle de concorrência.
- [ ] Evolução dos mecanismos de confiabilidade e integridade financeira.

### Fase 3 — Arquitetura de microsserviços

- [ ] Evolução dos módulos do monólito para serviços independentes.
- [ ] Implementação da comunicação entre serviços.
- [ ] Introdução de mensageria.
- [ ] Adaptação das operações financeiras à arquitetura distribuída.
