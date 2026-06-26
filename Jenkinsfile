pipeline {
    agent any

    tools {
        jdk 'Java21'
        maven 'Maven3'
    }

    stages {
        stage('1. Compilar e Testar') {
            steps {
                echo 'Executando testes unitários...'
                sh 'mvn clean test'
            }
        }
        
        stage('2. Gerar Imagem Docker com Jib (Modo Tarball)') {
            steps {
                echo 'Construindo imagem Docker em formato TAR com Java 21...'
                
                // Forçamos o Jib a usar a imagem base do Temurin com Java 21 LTS
                sh '''
                    mvn compile com.google.cloud.tools:jib-maven-plugin:3.4.1:buildTar \
                    -Dimage=ledger-reconciliation-api:latest \
                    -Djib.from.image=eclipse-temurin:21-jre \
                    -DskipTests
                '''
                
                echo 'Sucesso absoluto! O arquivo da imagem Java 21 foi gerado em: target/jib-image.tar'
            }
        }

        stage('3. Deploy Automático na Máquina Física') {
            steps {
                echo 'Carregando a imagem e instanciando a aplicação automaticamente...'
                sh '''
                    # 1. Atualiza a imagem no motor local da máquina física
                    docker load --input target/jib-image.tar
                    
                    # 2. Remove o container antigo da API (se ele existir) para não dar conflito de nome
                    docker rm -f ledger-reconciliation-api || true
                    
                    # 3. Sobe o novo container com as variáveis e portas mapeadas
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