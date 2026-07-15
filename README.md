# GitFlow Coach

API Spring Boot criada para apoiar equipes na adoção do Git, especialmente em ambientes que migraram de ferramentas centralizadas, como SVN, e ainda enfrentam dificuldades com branches, commits, pull requests e resolução de conflitos.

## Motivação

A implantação de uma nova ferramenta de versionamento não garante, por si só, que o processo de desenvolvimento evolua.

Em muitas equipes, a migração de SVN para Git mantém problemas como:

- dependência de poucas pessoas para criar branches e realizar integrações;
- receio de executar comandos Git;
- ausência de um fluxo padronizado;
- commits pouco descritivos;
- pull requests inconsistentes;
- baixa autonomia dos desenvolvedores;
- dificuldade para identificar em qual etapa do processo o time mais precisa de apoio.

O GitFlow Coach busca reduzir essa barreira por meio de orientação, padronização e automação.

## Objetivo

Fornecer uma API capaz de guiar desenvolvedores durante o fluxo de trabalho com Git, gerando sugestões seguras e padronizadas para:

- nomes de branches;
- mensagens de commit;
- comandos Git;
- checklists de desenvolvimento;
- abertura e revisão de pull requests;
- acompanhamento da evolução de tarefas.

## MVP

A primeira versão do projeto terá as seguintes funcionalidades:

- cadastro de projetos;
- cadastro de tarefas;
- associação de tarefas a projetos;
- geração automática de nomes de branches;
- criação automática de checklist para novas tarefas;
- sugestão de comandos Git;
- validação de mensagens com Conventional Commits;
- acompanhamento do status das tarefas.

## Exemplo de uso

Entrada:

```json
{
  "codigo": "216026",
  "titulo": "Ajuste no cálculo de apuração de custo",
  "tipo": "feat",
  "projetoId": 1
}
```

Saída esperada:

```json
{
  "codigo": "216026",
  "branchSugerida": "tarefa/216026-ajuste-calculo-apuracao-custo",
  "commitSugerido": "feat: ajusta cálculo de apuração de custo",
  "checklist": [
    "Atualizar a branch base",
    "Criar a branch da tarefa",
    "Realizar commits pequenos e descritivos",
    "Enviar a branch para o repositório remoto",
    "Abrir o pull request",
    "Solicitar revisão"
  ]
}
```

## Tecnologias

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- Bean Validation
- Maven
- JUnit
- Mockito
- Git e GitHub

## Arquitetura inicial

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Estrutura planejada:

```text
src/main/java/com/luisjagella/gitflowcoach
├── controller
├── dto
├── entity
├── exception
├── repository
└── service
```

## Modelo de domínio inicial

```text
Projeto 1 ─── N Tarefa
Tarefa  1 ─── N ChecklistItem
```

Entidades iniciais:

- `Projeto`: representa um repositório e suas convenções;
- `Tarefa`: representa uma atividade de desenvolvimento;
- `ChecklistItem`: representa uma etapa necessária para concluir a tarefa.

## Como executar

### Pré-requisitos

- Java 17 ou superior;
- Maven ou Maven Wrapper;
- MySQL em execução;
- banco de dados criado para a aplicação.

### Variáveis de ambiente

Defina a senha do banco:

```bash
DB_PASSWORD=sua_senha
```

Exemplo de configuração:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gitflow_coach
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

### Execução

No Windows:

```bash
mvnw.cmd spring-boot:run
```

Em Linux ou macOS:

```bash
./mvnw spring-boot:run
```

## Estratégia de desenvolvimento

O projeto será desenvolvido com:

- Issues para representar funcionalidades e melhorias;
- commits pequenos seguindo Conventional Commits;
- quadro Kanban para acompanhar o andamento;
- pull requests para registrar e revisar mudanças relevantes.

### Padrão de commits

```text
feat: adiciona nova funcionalidade
fix: corrige comportamento incorreto
refactor: melhora estrutura sem alterar comportamento
docs: atualiza documentação
test: adiciona ou ajusta testes
chore: altera configuração ou infraestrutura
```

## Roadmap

- [ ] Finalizar CRUD de projetos;
- [ ] Implementar CRUD de tarefas;
- [ ] Gerar nomes de branches automaticamente;
- [ ] Criar checklist padrão ao cadastrar tarefa;
- [ ] Gerar comandos Git conforme o contexto;
- [ ] Validar Conventional Commits;
- [ ] Adicionar tratamento global de exceções;
- [ ] Implementar autenticação e autorização;
- [ ] Integrar com a API do GitHub;
- [ ] Adicionar testes unitários e de integração;
- [ ] Dockerizar a aplicação;
- [ ] Criar interface web.

## Contexto acadêmico

O projeto também consolida conhecimentos estudados na pós-graduação em Engenharia de Software com Java e IA, incluindo APIs REST, Spring Boot, JPA, relacionamentos entre entidades, controle transacional, tratamento de exceções, segurança e observabilidade.

## Licença

Projeto desenvolvido para fins de estudo e portfólio.
