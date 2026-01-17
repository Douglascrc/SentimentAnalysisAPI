# SentimentAnalysisAPI

API para análise automática de sentimentos em textos, aplicada a avaliações de clientes, comentários em redes sociais e respostas a pesquisas de satisfação.  
O sistema utiliza modelos de Machine Learning integrados a uma aplicação back-end robusta, com persistência de dados e dashboard de acompanhamento.

---

## Visão Geral

O **SentimentAnalysisAPI** é um sistema completo de análise de sentimentos que classifica textos como positivos ou negativos, retornando também a probabilidade da predição e as palavras mais influentes na decisão do modelo.

O projeto foi desenvolvido como um MVP de hackathon, com foco em escalabilidade, clareza arquitetural e integração entre as áreas de **Back-end**, **Data Science** e **Front-end**.

A solução é indicada para empresas e equipes que precisam analisar grandes volumes de feedbacks de forma automatizada, reduzindo esforço manual e apoiando a tomada de decisão baseada em dados.

---

## Funcionalidades

- Análise de sentimentos em múltiplos idiomas (Português, Inglês e Espanhol)
- Classificação binária de sentimento (Positivo / Negativo)
- Retorno de probabilidade e nível de confiança da predição
- Identificação das palavras-chave mais influentes
- Persistência de comentários e predições com identificador UUID
- Dashboard com métricas e estatísticas em tempo real
- Validação robusta de entradas
- Tratamento global de exceções com códigos HTTP semânticos
- Explicabilidade do modelo de Machine Learning
- Respostas padronizadas em formato JSON
- Monitoramento de saúde da aplicação via Spring Actuator
- Configuração de CORS para integração com aplicações front-end

---

## Arquitetura

A aplicação segue uma arquitetura desacoplada, separando claramente as responsabilidades entre as camadas.

```text
Frontend (React)
        |
        v
Backend (Spring Boot - API REST)
        |
        v
Data Science Service (FastAPI + ML)
        |
        v
Banco de Dados (H2 / PostgreSQL)
````

### Componentes

* **Front-end**: Dashboard para visualização de métricas e simulação de análises
* **Back-end**: Orquestração das requisições, validações, persistência e comunicação com o serviço de ML
* **Data Science**: Modelos de Machine Learning responsáveis pelas predições
* **Banco de Dados**: Armazenamento de comentários, predições e logs

---

## Stack Tecnológica

### Back-end

* Java 17+
* Spring Boot 3.x
* Spring Data JPA
* Spring Boot Actuator
* Lombok
* Bean Validation
* Maven

### Banco de Dados

* PostgreSQL (ambiente de produção)
* JPA / Hibernate
* UUID como identificador primário

### Data Science

* Python 3.9+
* FastAPI
* Pydantic
* scikit-learn
* NumPy
* joblib
* Uvicorn

### Machine Learning

* TF-IDF Vectorization
* Logistic Regression
* Pipeline completo do scikit-learn
* Modelos independentes por idioma

### Front-end

* React
* Vite
* Tailwind CSS
* Recharts

---

## Instalação

### Pré-requisitos

* Java 17 ou superior
* Maven 3.6+
* Python 3.9+
* Git
* Docker (opcional)

### Clonando o repositório

```bash
git clone https://github.com/Douglascrc/SentimentAnalysisAPI.git
cd SentimentAnalysisAPI
```

---

## Serviço de Data Science

O serviço de Data Science é responsável por carregar automaticamente os modelos treinados e expor os endpoints de predição.

### Execução local

```bash
cd data-science
pip install -r requirements.txt
uvicorn app:app --host 0.0.0.0 --port 3000 --reload
```

### Execução via Docker

```bash
cd data-science
docker build -t sentiment-api .
docker run -p 3000:3000 sentiment-api
```

### Documentação interativa

* Swagger UI: [http://localhost:3000/docs](http://localhost:3000/docs)
* ReDoc: [http://localhost:3000/redoc](http://localhost:3000/redoc)

---

## Back-end (Spring Boot)

### Execução

```bash
mvn clean install
mvn spring-boot:run
```

A aplicação estará disponível em:

* **API**: [http://localhost:8080](http://localhost:8080)

---

## Uso da API

### Análise de Sentimento

**POST** `/sentiment`

```json
{
  "text": "Produto excelente, superou minhas expectativas",
  "language": "pt"
}
```

**Resposta:**

```json
{
  "previsao": "Positivo",
  "probabilidade": 0.92,
  "idioma": "pt",
  "palavras_chave": ["excelente", "superou", "expectativas"]
}
```

### Estatísticas

**GET** `/stats`

Retorna métricas agregadas do sistema, como total de análises realizadas, percentuais de sentimentos e tempo médio de resposta.

---

## Modelo de Machine Learning

### Estrutura

Cada idioma possui um modelo independente treinado com um pipeline do scikit-learn:

* TF-IDF Vectorizer
* Logistic Regression

### Características

* Retorno de probabilidades
* Extração das palavras mais influentes na predição
* Threshold fixo de 0.5 para decisão de classe
* Versionamento de modelos

### Dataset

O modelo em português foi treinado com o dataset **B2W**, contendo avaliações reais de produtos.

---

## Validações e Tratamento de Erros

* Validação de campos obrigatórios
* Validação de tamanho mínimo de texto
* Tratamento global de exceções
* Respostas padronizadas para erros de validação, indisponibilidade de serviço e falhas inesperadas

---

## Desenvolvimento

### Padrões Utilizados

* Repository Pattern
* Service Layer
* DTO Pattern
* Global Exception Handler

---

## Deploy

### Aplicação Online

Acesse a aplicação funcionando em produção:

**[https://sentiment-analysis-frontend-gamma.vercel.app](https://sentiment-analysis-frontend-gamma.vercel.app)**

<details>
<summary>Endpoints da API</summary>

- **Backend (Java)**: `https://sentimentanalysisapi-backend.onrender.com`
- **Data Science (Python)**: `https://sentimentanalysisapi-data.onrender.com`
</details>



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
