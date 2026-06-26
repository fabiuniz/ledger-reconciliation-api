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
                echo 'Carregando a imagem e reiniciando a aplicação automaticamente...'
                sh '''
                    # 1. Carrega a imagem no motor Docker comum
                    docker load --input target/jib-image.tar
                    
                    # 2. Executa o compose apontando para o arquivo REAL da sua máquina física
                    docker compose -f /home/userlnx/docker/script_docker/java-ia/docker-compose.yml up -d --no-deps ledger-reconciliation-api
                '''
            }
        }
    }
}