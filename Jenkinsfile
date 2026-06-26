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
    }
}