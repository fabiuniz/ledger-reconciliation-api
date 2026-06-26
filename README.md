# 📋 Prancheta de Trabalho: Ledger Reconciliation AI API (TQI FinTech Edition)

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple?style=for-the-badge&logo=kotlin)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-9092-black?style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/)
[![AWS SQS](https://img.shields.io/badge/AWS_SQS-LocalStack-FF9900?style=for-the-badge&logo=amazonaws)](https://aws.amazon.com/sqs/)
[![Google Gemini](https://img.shields.io/badge/Gemini_2.5_Flash-Real_AI-blue?style=for-the-badge&logo=googlegemini)](https://ai.google.dev/)

Esta prancheta guia o desenvolvimento passo a passo de um microsserviço de **Conciliação Bancária e Liquidação de Missão Crítica** IA-Native utilizando **Java 21 (Virtual Threads)**, **Kotlin (Coroutines)**, **Arquitetura Hexagonal**, **Mensageria Híbrida (Kafka/SQS)** e tolerância a falhas.

---

## 🚀 Fase 1: Setup do Ambiente e Infraestrutura

**Objetivo:** Levantar toda a infraestrutura local necessária simulando o ambiente de nuvem AWS e os brokers de produção de forma efêmera através do Docker.

- [x] **Task 1.1: Alinhamento de SDKs**
  - Instalar o JDK 21 e configurar a IDE (IntelliJ IDEA) com suporte combinado para Java e Kotlin focado em alta precisão matemática (`BigDecimal`).
- [x] **Task 1.2: Orquestração de Containers (`docker-compose.yml`)**
  - Configurar banco relacional **PostgreSQL** (porta `5432`) preparado para dados transacionais pesados.
  - Configurar **Apache Kafka** (porta `9092`) e **Zookeeper** para streaming de eventos de liquidação.
  - Configurar **LocalStack** (porta `4566`) para emular os serviços de nuvem AWS localmente (SQS).
  - Configurar malha de observabilidade com **Prometheus** (porta `9090`) e **Grafana** (porta `3000`).
- [x] **Task 1.3: Script de Inicialização da Cloud Local (`init-aws.sh`)**
  - Criar um script automatizado para o LocalStack criar a fila SQS `ledger-clearing-upload-queue`.
- [x] **Task 1.4: Sanity Check da Infraestrutura**
  - Executar o reset atômico para limpar conflitos de rede e subir os 6 contêineres com status `Up`.

---

## 📑 Fase 2: Design First & Spec-Driven Development (SDD)

**Objetivo:** Definir os contratos antes de encostar na lógica do backend, utilizando a especificação OpenAPI como a única fonte da verdade do projeto.

- [x] **Task 2.1: Definição da Spec (`api-spec.yaml`)**
  - Criar e validar o contrato OpenAPI 3.0 contendo os caminhos síncronos e assíncronos de core bancário.
- [x] **Task 2.2: Construção do `pom.xml` Híbrido**
  - Adicionar plugins de compilação cruzada para Java e Kotlin e dependências do ecossistema (*Spring Boot 3.2.5*, *HikariCP*, *AWS SQS*, *Kafka*, *Resilience4j*, *Actuator*).
- [x] **Task 2.3: Geração Assistida do Esqueleto (Hexagonal)**
  - Criar a estrutura rígida de pacotes dividida entre `domain`, `application` (ports) e `infrastructure` (adapters).

---

## ☕ Fase 3: Domínio Avançado e Concorrência Moderna

**Objetivo:** Implementar o coração do negócio, explorando concorrência de alta performance sem desperdício de memória ou CPU através de recursos modernos do Java e Kotlin.

- [x] **Task 3.1: Regras do Domínio com Java 21**
  - Implementar o modelo de dados `ReconciliationBatch` blindando as regras contra erros de centavos.
- [ ] **Task 3.2: Processamento Paralelo via Virtual Threads**
  - No método `gerarDashboard()`, utilizar `Executors.newVirtualThreadPerTaskExecutor()` para cruzamento de grandes volumes de registros de forma não-bloqueante.
- [x] **Task 3.3: Lógica Preditiva Suspended com Kotlin**
  - Criar o `ReconciliationAnalysisService.kt` utilizando funções suspensíveis (`suspend fun`) para I/O de auditoria.
- [x] **Task 3.4: Concorrência Estruturada com `async/await`**
  - Paralelizar via escopo concorrente (`coroutineScope`) o cálculo estatístico de score de discrepância e verificação de conformidade.

---

## 📨 Fase 4: Mensageria, IA Multimodal e Resiliência

**Objetivo:** Ligar o fluxo de ponta a ponta de forma assíncrona, conectando o processamento do arquivo de lote à inteligência artificial do Gemini para análise inteligente de quebras de arredondamento.

- [x] **Task 4.1: Endpoint de Upload de Arquivos**
  - Criar o controller REST para capturar o payload multipart de arquivos de extrato (`/api/reconciliations/upload`).
- [x] **Task 4.2: Enfileiramento via `SqsTemplate`**
  - Despachar o payload do lote recebido para a fila do Amazon SQS local de forma rápida (Retorno `202 Accepted`).
- [x] **Task 4.3: Consumidor AWS SQS Reativo**
  - Implementar o `LedgerSqsListener` para escutar e processar novos lotes de extratos que chegam na fila.
- [x] **Task 4.4: Integração com o Motor de Auditoria (Gemini 2.5 Real)**
  - Configurar a classe `ReconciliationAiEngine` e mapear a injeção do token no `application.yml` via `${GOOGLE_AI_KEY:}` para bater diretamente na API oficial do Google AI Studio.
- [x] **Task 4.5: Proteção por Circuit Breaker (Resilience4j)**
  - Isolar a chamada da IA com `@CircuitBreaker`, garantindo o acionamento automático do `fallbackAnalyse` em cenários de queda ou erro `403 Forbidden` externo.
- [x] **Task 4.6: Publicação no Barramento Apache Kafka**
  - Produzir e postar o evento de negócio estruturado com consistência forte (`acks = -1`) no tópico `ledger-reconciliation-events`.
- [x] **Task 4.7: Persistência Relacional**
  - Realizar a gravação oficial e consolidação dos lotes processados no banco PostgreSQL com o pool de conexões estável do HikariCP.

---

## 📊 Fase 5: Qualidade de Código e Observabilidade

**Objetivo:** Garantir a robustez sistêmica com testes modernos, além de expor telemetria detalhada para visualização do comportamento da API.

- [ ] **Task 5.1: Testes de Unidade Estruturados (Java)**
  - Validar as regras de negócio e asserções matemáticas de centavos utilizando JUnit 5 e Mockito.
- [x] **Task 5.2: Testes de Coroutines (Kotlin)**
  - Escrever e consolidar a suíte de testes específicos para o fluxo assíncrono do Kotlin (`ReconciliationAnalysisServiceTest.kt`).
- [x] **Task 5.3: Telemetria com Prometheus e Actuator**
  - Expôr os endpoints do Spring Actuator em `/actuator/prometheus` integrados com o contêiner do Prometheus.
- [x] **Task 5.4: Teste End-to-End (E2E) de Sucesso**
  - Realizar o disparo real com um arquivo de extrato de teste via cURL e validar toda a esteira reativa e o isolamento do Circuit Breaker.
- [x] **Task 5.5: Painel Visual Mission Control (Live Telemetry)**
  - Construção de interface front-end reativa acoplada em `static/index.html` consumindo buffer de eventos via polling para demonstrações executivas.
---

## 🛠️ Guia de Execução (Terminal Comandos)

### 🚜 Reset Atômico da Infraestrutura (Docker)
```bash

docker rm -f ledger-postgres ledger-localstack ledger-zookeeper ledger-kafka ledger-prometheus ledger-grafana 2>/dev/null
docker-compose up -d --remove-orphans

```

### 🚀 Inicialização Indestrutível da Aplicação (Injeção de .env)
```bash

set -a && source .env && set +a && mvn spring-boot:run

```
### 🧪 Disparar Carga no Pipeline Contábil (cURL)
```bash

curl -X POST -F "file=@./extrato-teste.csv" http://localhost:8080/api/reconciliations/upload

```
### 🧪 Jenkins
```bash
docker run -d \
  -p 8181:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  --name jenkins-server \
  --restart unless-stopped \
  jenkins/jenkins:lts

docker logs jenkins-server
chmod 666 /var/run/docker.sock
```