# Pós-Graduação Facens — Desenvolvimento FullStack
## Atividade: API RESTful com Spring Boot, JPA e Exercícios Práticos

**Professor:** Fabrício Tonetto Londero  
**Tecnologias:** Java 17, Spring Boot 3.2.5, Spring Web, Spring Data JPA, H2 Database, SpringDoc OpenAPI (Swagger), Maven.

---

## 📌 Sumário do Projeto

Este repositório contém a implementação completa de todo o conteúdo ministrado em aula, além da resolução dos **Exercícios 1 e 2** propostos nos slides da disciplina.

1. **Conteúdo Prático de Aula:**
   - Configuração de projeto Spring Boot no Spring Initializr com Maven.
   - Rotas HTTP básicas (`GET`, `POST`) utilizando `@PathVariable` e `@RequestBody`.
   - Gerenciamento de lista em memória (`List<Pessoa>`) e persistência relacional com Spring Data JPA e H2 Database.
   - Relacionamento de entidades ORM (`Autor` 1:N `Livro`), demonstrando estratégias de carregamento **Lazy Loading** (`FetchType.LAZY`) e **Eager Loading** (`FetchType.EAGER`), além de consultas otimizadas com HQL/JPQL (`JOIN FETCH`).
   - Carga inicial de dados de teste via `@Bean CommandLineRunner`.
   - Documentação interativa e testes visuais com **Swagger UI (OpenAPI 3.0)** e console do banco de dados em memória **H2 Console**.

2. **Exercício 1 (Slide 40):**
   - `GET /hello/{nome}` — Retorna saudação personalizada: `"Olá, {nome}!"`.
   - `GET /calc/soma/{a}/{b}` e `/calc/soma?a=10&b=5` — Retorna soma em JSON: `{"resultado": 15}`. Se faltar `a` ou `b`, retorna erro `400 Bad Request` com mensagem explicativa.
   - `GET /temperatura/{valor}/{de}/{para}` e `/temperatura/convert?valor=30&de=C&para=F` — Conversão bidirecional entre Celsius (`C`), Fahrenheit (`F`) e Kelvin (`K`). Valida unidades permitidas e retorna erro `400 Bad Request` se a unidade for inválida.

3. **Exercício 2 (Slide 49):**
   - `PUT /pessoa/{id}` — Substituição completa do recurso (id e nome), retornando o objeto atualizado (200 OK) ou 404 caso não exista.
   - `PATCH /pessoa/{id}` — Atualização parcial do recurso (ex.: altera somente o nome), mantendo os demais dados inalterados (200 OK) ou 404 caso não exista.
   - `DELETE /pessoa/{id}` — Remoção da pessoa por ID, retornando o status HTTP `204 No Content` conforme a especificação RESTful.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- **Java JDK 17** instalado na máquina.
- Git instalado (opcional, para versionamento).

### Passo a passo pelo terminal

1. Clone o repositório ou acerte a pasta do projeto:
```bash
cd pos-facens-springboot-api
```

2. Execute o Maven Wrapper para iniciar a aplicação:
- No Windows:
```cmd
.\mvnw.cmd spring-boot:run
```
- No Linux / macOS:
```bash
./mvnw spring-boot:run
```

3. A aplicação estará ativa em: `http://localhost:8080`

### Execução de Testes Automatizados
O projeto conta com suíte de testes de integração automatizados (`MockMvc`) cobrindo 100% dos requisitos da aula e dos exercícios.
Para rodar os testes:
```cmd
.\mvnw.cmd test
```

---

## 📖 Documentação da API & Ferramentas

- **Swagger UI (Documentação Interativa):**  
  Acesse no navegador: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **H2 Console (Banco de Dados em Memória):**  
  Acesse no navegador: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
  - *JDBC URL:* `jdbc:h2:mem:testdb`  
  - *User Name:* `sa`  
  - *Password:* (vazio)

---

## 🛠️ Detalhamento dos Endpoints e Resolução dos Exercícios

### 1. Rotas Básicas e Exercício 1

#### 1.1 `GET /hello/{nome}` (Exercício 1 - Item 1)
- **Descrição:** Retorna a saudação no formato `"Olá, {nome}!"`.
- **Exemplo de Requisição:** `GET /hello/Fabricio`
- **Resposta:** (HTTP 200)
```text
Olá, Fabricio!
```

#### 1.2 `GET /calc/soma/{a}/{b}` ou `/calc/soma?a=10&b=5` (Exercício 1 - Item 2)
- **Descrição:** Retorna a soma de dois números inteiros. Suporta tanto parâmetros de rota (PathVariable) quanto query parameters. Valida a presença obrigatória dos dois operandos.
- **Exemplo de Requisição:** `GET /calc/soma/10/5` ou `GET /calc/soma?a=10&b=5`
- **Resposta Sucesso:** (HTTP 200)
```json
{
  "resultado": 15
}
```
- **Exemplo com Parâmetro Ausente:** `GET /calc/soma?a=10`
- **Resposta de Erro:** (HTTP 400 Bad Request)
```json
{
  "erro": "O parâmetro 'b' é obrigatório."
}
```

#### 1.3 `GET /temperatura/{valor}/{de}/{para}` ou `/temperatura/convert?valor=30&de=C&para=F` (Exercício 1 - Item 3)
- **Descrição:** Converte valores de temperatura entre Celsius (`C`), Fahrenheit (`F`) e Kelvin (`K`).
- **Fórmulas aplicadas:**
  - $C \to F: (C \times 9/5) + 32$
  - $C \to K: C + 273.15$
  - $F \to C: (F - 32) \times 5/9$
  - $K \to C: K - 273.15$
- **Exemplo de Requisição:** `GET /temperatura/30/C/F`
- **Resposta Sucesso:** (HTTP 200)
```json
{
  "valorOriginal": 30.0,
  "de": "C",
  "para": "F",
  "resultado": 86.0
}
```
- **Exemplo com Unidade Inválida:** `GET /temperatura/30/X/F`
- **Resposta de Erro:** (HTTP 400 Bad Request)
```json
{
  "status": 400,
  "erro": "Unidade inválida informada ('X' ou 'F'). Apenas 'C', 'F' e 'K' são permitidos."
}
```

---

### 2. Gerenciamento de Pessoas e Exercício 2

Baseado na lista gerenciada pela aplicação (`/pessoa`), os métodos implementados incluem:

| Método | Endpoint | Descrição | Status HTTP |
| :--- | :--- | :--- | :--- |
| `POST` | `/pessoa/pessoa` | Cadastra uma nova pessoa na lista | `201 Created` |
| `GET` | `/pessoa/pessoas` | Lista todas as pessoas cadastradas | `200 OK` |
| `GET` | `/pessoa/pessoa/{nome}` | Busca uma pessoa pelo nome | `200 OK` |
| `GET` | `/pessoa/{id}` | Busca uma pessoa pelo ID | `200 OK` / `404 Not Found` |
| **`PUT`** | **`/pessoa/{id}`** | **Exercício 2:** Substitui integralmente o registro (id e nome) | `200 OK` / `404 Not Found` |
| **`PATCH`**| **`/pessoa/{id}`** | **Exercício 2:** Atualiza parcialmente (ex.: só o nome) | `200 OK` / `404 Not Found` |
| **`DELETE`**| **`/pessoa/{id}`** | **Exercício 2:** Remove a pessoa da lista | `204 No Content` / `404 Not Found` |

#### Exemplos de Uso do Exercício 2:

- **PUT `/pessoa/1`** (Substituição Completa):
  - *Payload:*
    ```json
    {
      "id": 1,
      "nome": "Fabrício Tonetto Londero"
    }
    ```
  - *Resposta (200 OK):*
    ```json
    {
      "id": 1,
      "nome": "Fabrício Tonetto Londero"
    }
    ```

- **PATCH `/pessoa/1`** (Atualização Parcial):
  - *Payload:*
    ```json
    {
      "nome": "Fabrício Londero (Editado Parcial)"
    }
    ```
  - *Resposta (200 OK):*
    ```json
    {
      "id": 1,
      "nome": "Fabrício Londero (Editado Parcial)"
    }
    ```

- **DELETE `/pessoa/1`** (Exclusão):
  - *Resposta:* `HTTP 204 No Content` (sem corpo na resposta)

---

### 3. ORM, JPA, Relacionamentos e HQL (Slides de Aula)

- `GET /autores-lazy`: Demonstra busca de autores com carregamento preguiçoso (`FetchType.LAZY`) de livros.
- `GET /livros-eager`: Demonstra busca de livros com carregamento antecipado (`FetchType.EAGER`) do autor associado.
- Os dados são pré-carregados automaticamente na inicialização com o autor "Machado de Assis" e as obras "Dom Casmurro" e "Memórias Póstumas".

---

## 📦 Como Subir para o seu GitHub

Para entregar o link do repositório ao professor:

1. No terminal, dentro da pasta do projeto (`pos-facens-springboot-api`):
```bash
git init
git add .
git commit -m "feat: implementacao completa das aulas e resolucao dos exercicios 1 e 2"
```

2. Crie um repositório no seu GitHub (ex.: `pos-facens-springboot-api`).

3. Vincule a branch remota e envie os commits:
```bash
git branch -M main
git remote add origin https://github.com/<SEU_USUARIO>/<SEU_REPOSITORIO>.git
git push -u origin main
```
