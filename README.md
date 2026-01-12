# SentimentAnalysisAPI

**SentimentAnalysisAPI** é uma solução completa para análise automática de sentimentos em textos como avaliações de clientes, comentários em redes sociais e respostas a pesquisas de satisfação.

A API recebe um texto, processa através de um modelo de Machine Learning e retorna automaticamente a **classificação do sentimento**, **probabilidade** e **palavras-chave mais influentes** na decisão.

O projeto atende empresas das áreas de **Atendimento ao Cliente**, **Marketing** e **Operações**, que precisam entender rapidamente se os feedbacks são positivos ou negativos, sem a necessidade de leitura manual de grandes volumes de dados.

Desenvolvido como um **MVP de hackathon**, o sistema integra um modelo de **Data Science** à aplicação **Back-end**, com **persistência de dados** para histórico e análises futuras, retornando previsões em **formato JSON** para fácil integração com outras aplicações.

---

## Índice
- [Funcionalidades](#funcionalidades)
- [Stack Utilizada](#stack-utilizada)
- [Arquitetura do Projeto](#arquitetura-do-projeto)
- [Rodando Localmente](#rodando-localmente)
- [Referência da API](#referência-da-api)
- [Validações Implementadas](#validações-implementadas)
- [Sobre o Modelo](#sobre-o-modelo)
- [Exemplos de Uso](#exemplos-de-uso)
- [Tecnologias e Padrões](#tecnologias-e-padrões)
- [Desenvolvimento](#desenvolvimento)
- [Roadmap](#roadmap)
- [Autores](#autores)

---

## Funcionalidades
- ✅ Receber textos para análise de sentimento via requisição HTTP
- ✅ Classificar feedbacks como **Positivo** ou **Negativo**
- ✅ Retornar a previsão com **probabilidade associada** e confiança da predição
- ✅ Identificar **palavras-chave mais influentes** na decisão do modelo
- ✅ Persistir comentários e predições em banco de dados
- ✅ Registrar logs de requisições para monitoramento e auditoria
- ✅ Validar dados de entrada (mínimo de 5 caracteres, não apenas números)
- ✅ Fornecer **explicabilidade** sobre as decisões do modelo
- ✅ Tratamento global de exceções com respostas padronizadas e códigos HTTP semânticos
- ✅ Retornar respostas em **formato JSON** para fácil integração

---

## Stack Utilizada
### Back-end
- **Java 17+**
- **Spring Boot** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Lombok** - Redução de boilerplate
- **RestTemplate** - Cliente HTTP
- **Maven** - Gerenciamento de dependências

### Banco de Dados
- **H2 Database** - Desenvolvimento
- **PostgreSQL** - Produção (suportado)
- **JPA/Hibernate** - ORM

### Data Science
- **Python 3.9+**
- **FastAPI** - Framework web assíncrono
- **scikit-learn** - Machine Learning
- **joblib** - Serialização do modelo
- **uvicorn** - Servidor ASGI

### Modelagem
- **TF-IDF** - Vetorização de texto
- **Logistic Regression** - Classificação binária
- **Pipeline integrado** - Processamento completo

### Dataset
- **B2W** - Avaliações reais de produtos em português

### Ferramentas
- **Git & GitHub** - Controle de versão
- **Postman** - Testes de API
- **Docker** - Containerização
- **Jupyter Notebook** - Experimentação

---

## Arquitetura do Projeto

```
SentimentAnalysisAPI/
├── src/
│   ├── main/
│   │   ├── java/com/hackathon/sentiment/api/
│   │   │   ├── SentimentAnalysisApiApplication.java
│   │   │   ├── controller/
│   │   │   │   └── SentimentController.java
│   │   │   ├── dto/
│   │   │   │   ├── SentimentRequest.java
│   │   │   │   ├── SentimentResponse.java
│   │   │   │   └── ErrorResponse.java
│   │   │   ├── entity/
│   │   │   │   ├── Comment.java
│   │   │   │   ├── SentimentPrediction.java
│   │   │   │   └── RequestLog.java
│   │   │   ├── repository/
│   │   │   │   ├── CommentRepository.java
│   │   │   │   ├── SentimentPredictionRepository.java
│   │   │   │   └── RequestLogRepository.java
│   │   │   ├── service/
│   │   │   │   ├── SentimentService.java
│   │   │   │   └── TextValidationService.java
│   │   │   └── exception/
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-local.yml
│   │       └── application.properties
│   └── test/
│       └── java/com/hackathon/sentiment/api/
│           ├── exception/
│           │   └── GlobalExceptionHandlerTest.java
│           ├── service/
│           │   └── TextValidationServiceTest.java
│           └── SentimentAnalysisApiApplicationTests.java
│
└── data-science/
    ├── __pycache__/
    ├── models/
    │   └── sentiment_model_pipeline.pkl
    ├── notebooks/
    │   └── sentiment_model_B2W_pt.ipynb
    ├── app.py
    ├── Dockerfile
    └── requirements.txt
```

---
---

## Rodando Localmente
### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- Python 3.9 ou superior
- Git

### 1. Clonando o repositório

```bash
git clone https://github.com/Douglascrc/SentimentAnalysisAPI.git
cd SentimentAnalysisAPI
```

### 2. Configurando variáveis de ambiente

Crie um arquivo `application-local.yml` em `src/main/resources/`:

```yaml
env:
  PYTHON_SERVICE_URL: http://localhost:3000/predict

spring:
  datasource:
    url: jdbc:h2:mem:sentimentdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  
  h2:
    console:
      enabled: true
      path: /h2-console
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### 3. Executando o serviço de Data Science

#### Opção A: Localmente

```bash
cd data-science
pip install -r requirements.txt
uvicorn app:app --host 0.0.0.0 --port 3000 --reload
```

#### Opção B: Com Docker

```bash
cd data-science
docker build -t sentiment-api .
docker run -p 3000:3000 sentiment-api
```

**A API de Data Science estará disponível em:**
```
http://localhost:3000
```

**Documentação interativa:**
- Swagger UI: http://localhost:3000/docs
- ReDoc: http://localhost:3000/redoc

### 4. Executando o Back-end

```bash
mvn clean install
mvn spring-boot:run
```

**A aplicação principal estará disponível em:**
```
http://localhost:8080
```

**Console H2 Database:**
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:sentimentdb`
- Username: `sa`
- Password: (deixe em branco)

---

## Referência da API
### POST `/sentiment`

Classifica o sentimento de um texto enviado pelo usuário.

#### Request Body

```json
{
  "text": "O atendimento foi excelente e muito rápido"
}
```

#### Response — Sucesso (200)

```json
{
  "previsao": "Positivo",
  "probabilidade": 0.87,
  "palavras_chave": ["excelente", "rápido", "atendimento"]
}
```

#### Response — Erro de Validação (400)

```json
{
  "comentario": "Erro",
  "probabilidade": 0.0,
  "palavras_chave": [],
  "mensagem": "O texto deve ter no mínimo 5 caracteres"
}
```

#### Response — Serviço Indisponível (503)

```json
{
  "comentario": "Erro",
  "probabilidade": 0.0,
  "palavras_chave": [],
  "mensagem": "Serviço de análise temporariamente indisponível. Tente novamente em instantes."
}
```

#### Response — Erro Interno (500)

```json
{
  "comentario": "Erro",
  "probabilidade": 0.0,
  "palavras_chave": [],
  "mensagem": "Não foi possível realizar a classificação no momento. Tente novamente mais tarde."
}
```

---

### POST `/predict` (Data Science - Porta 3000)

Endpoint direto do modelo de Machine Learning para testes ou integração direta.

#### Request Body

```json
{
  "text": "Este produto é excelente, muito satisfeito com a compra!"
}
```

#### Response — Sucesso (200)

```json
{
  "previsao": "Positivo",
  "probabilidade": 0.87,
  "palavras_chave": ["excelente", "satisfeito", "produto"]
}
```

---

## Validações Implementadas
A API implementa validações em múltiplas camadas para garantir a qualidade dos dados.

### TextValidationService (Back-end)

1. **Campo obrigatório**: O texto não pode ser nulo ou vazio
2. **Tamanho mínimo**: Pelo menos 5 caracteres
3. **Conteúdo válido**: Não pode conter apenas números

### GlobalExceptionHandler

Captura e trata exceções globalmente com códigos HTTP semânticos:

| Exceção | Código HTTP | Descrição |
|---------|-------------|-----------|
| `IllegalArgumentException` | 400 | Erros de validação |
| `RestClientException` | 503 | Falha na comunicação com Data Science |
| `Exception` | 500 | Erros inesperados do sistema |

### Fluxo de Processamento

1. **Recebimento**: Usuário envia texto via POST `/sentiment`
2. **Validação**: `TextValidationService` valida o texto
3. **Persistência**: Comentário é salvo na tabela `Comment`
4. **Predição**: Texto é enviado para o serviço Python via `RestTemplate`
5. **Armazenamento**: Predição é salva na tabela `SentimentPrediction`
6. **Resposta**: Resultado retornado com sentimento, probabilidade e palavras-chave

---

## Sobre o Modelo
O modelo é um **pipeline completo** que inclui:

1. **TF-IDF Vectorizer**: Transforma texto em features numéricas
2. **Logistic Regression**: Classificador binário de sentimentos

### Características

- **Explicabilidade**: Retorna palavras-chave mais influentes
- **Probabilidade**: Nível de confiança da predição
- **Pipeline integrado**: Processamento e predição em uma única etapa
- **Versionamento**: Cada predição registra a versão do modelo

### Dataset de Treinamento

Treinado com o dataset **B2W**, contendo avaliações reais de produtos em português brasileiro.

### Idiomas Suportados

- Português Brasileiro (otimizado)
- Inglês
- Espanhol

### Limitações

- Melhor performance com textos de avaliações de produtos
- Textos muito curtos (< 5 caracteres) não são processados
- Retorna sentimentos binários (positivo/negativo)

---

## Exemplos de Uso
### Texto Positivo

```bash
curl -X POST http://localhost:8080/sentiment \
  -H "Content-Type: application/json" \
  -d '{"text": "Produto excelente, superou minhas expectativas!"}'
```

**Resposta:**
```json
{
  "previsao": "Positivo",
  "probabilidade": 0.92,
  "palavras_chave": ["excelente", "superou", "expectativas"]
}
```

### Texto Negativo

```bash
curl -X POST http://localhost:8080/sentiment \
  -H "Content-Type: application/json" \
  -d '{"text": "Péssima experiência, não recomendo este produto."}'
```

**Resposta:**
```json
{
  "previsao": "Negativo",
  "probabilidade": 0.88,
  "palavras_chave": ["péssima", "não", "recomendo"]
}
```

### Erro de Validação

```bash
curl -X POST http://localhost:8080/sentiment \
  -H "Content-Type: application/json" \
  -d '{"text": "Bom"}'
```

**Resposta:**
```json
{
  "comentario": "Erro",
  "probabilidade": 0.0,
  "palavras_chave": [],
  "mensagem": "O texto deve ter no mínimo 5 caracteres"
}
```

---

## Tecnologias e Padrões
### Design Patterns

- **Repository Pattern**: Acesso aos dados através de interfaces
- **Service Layer**: Lógica de negócio isolada dos controladores
- **DTO Pattern**: Transferência de dados entre camadas
- **Global Exception Handler**: Tratamento centralizado de exceções

### Anotações Spring

- `@SpringBootApplication`: Configuração principal da aplicação
- `@RestController`: Controladores REST
- `@Service`: Camada de serviços
- `@Repository`: Camada de persistência
- `@Entity`: Entidades JPA
- `@RestControllerAdvice`: Tratamento global de exceções

### Lombok

- `@Data`: Getters, Setters, toString, equals, hashCode
- Records Java: DTOs imutáveis

---

## Desenvolvimento
### Retreinar o Modelo

Para retreinar ou modificar o modelo:

```bash
cd data-science
jupyter notebook notebooks/sentiment_model_B2W_pt.ipynb
```

Após o treinamento:
1. Salve o novo pipeline na pasta `models/`
2. Atualize a versão no `SentimentService.java`

### Métricas e Performance

Para informações detalhadas sobre métricas de desempenho (acurácia, precisão, recall, F1-score), consulte o notebook de treinamento em `notebooks/`.

### Executando Testes

```bash
# Rodar todos os testes
mvn test

# Rodar com cobertura
mvn test jacoco:report
```

### Acessando o Banco de Dados H2

Durante o desenvolvimento:

1. Acesse: http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:sentimentdb`
3. Username: `sa`
4. Password: (deixe em branco)

---

## Roadmap
### Convenção de Commits

Seguimos o padrão [Conventional Commits](https://www.conventionalcommits.org/):

- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `style`: Formatação
- `refactor`: Refatoração de código
- `test`: Testes
- `chore`: Tarefas de manutenção

---

## Autores
- **Douglas Campos** – [GitHub](https://github.com/douglascamposcr) |  [LinkedIn](https://www.linkedin.com/in/douglasbuscampos)
- **Ezequiel Elisel** – [GitHub](https://github.com/EzequielFarias144) |  [LinkedIn](https://www.linkedin.com/in/ezequiel-farias-5a6b813a2)
- **Isabelle Victoria Souza** – [GitHub](https://github.com/IsisVct) |  [LinkedIn](https://www.linkedin.com/in/isabelle-victoria)
- **João Victor Lima** – [GitHub](https://github.com/JotaveLima) |  [LinkedIn](https://www.linkedin.com/in/joao-victor-b-lima-)
- **Luana Romualdo** – [GitHub](https://github.com/usuario) |  [LinkedIn](https://www.linkedin.com/in/luanaromualdo-)
- **Mauro Pinheiro** – [GitHub](https://github.com/mauro-pinheiro51) |  [LinkedIn](https://www.linkedin.com/in/mauro-alves-pinheiro-8001073a1)
- **Vithor Elias** – [GitHub](https://github.com/Landon18) |  [LinkedIn](https://www.linkedin.com/in/vithor-narciso/)
---

**⭐ Se este projeto foi útil para você, considere dar uma estrela no GitHub!**
