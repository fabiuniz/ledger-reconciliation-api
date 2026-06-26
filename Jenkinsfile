pipeline {
    agent any

    options {
        // Impede que o Jenkins faça o checkout automático problemático
        skipDefaultCheckout(true) 
    }

    tools {
        jdk 'Java21'
        maven 'Maven3'
    }

    stages {
        stage('0. Inicializar e Clonar') {
            steps {
                echo 'Limpando workspace antigo e clonando repositório do zero...'
                cleanWs() // Apaga tudo que está corrompido na pasta
                checkout scm // Faz o clone limpo e correto
            }
        }

        stage('1. Compilar e Testar') {
            steps {
                echo 'Executando testes unitários...'
                sh 'mvn clean test'
            }
        }
        
        stage('2. Gerar Imagem Docker com Jib (Modo Tarball)') {
            steps {
                echo 'Construindo imagem Docker em formato TAR com Java 21...'
                sh '''
                    mvn compile com.google.cloud.tools:jib-maven-plugin:3.4.1:buildTar \
                    -Dimage=ledger-reconciliation-api:latest \
                    -Djib.from.image=eclipse-temurin:21-jre \
                    -DskipTests
                '''
            }
        }

        stage('3. Deploy Automático na Máquina Física') {
            steps {
                echo 'Carregando a imagem e instanciando a aplicação automaticamente...'
                sh '''
                    docker load --input target/jib-image.tar
                    docker rm -f ledger-reconciliation-api || true
                    docker run -d \
                      --name ledger-reconciliation-api \
                      -p 8080:8080 \
                      --network host \
                      -e SPRING_PROFILES_ACTIVE=local \
                      -e GOOGLE_AI_KEY=sua_chave_do_gemini_aqui \
                      ledger-reconciliation-api:latest
                '''
                echo 'Sucesso! Aplicação rodando em http://localhost:8080'
            }
        }
    }
}