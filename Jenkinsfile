pipeline {
    agent any

    tools {
        jdk 'Java21'
        maven 'Maven3'
    }

    environment {
        // Forçando o caminho físico padrão do Jenkins para ferramentas instaladas
        JAVA_HOME = '/var/jenkins_home/tools/hudson.model.JDK/Java21'
        PATH      = "${env.JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('1. Compilar e Testar') {
            steps {
                echo 'Executando testes unitários com Java 21...'
                sh 'mvn clean test'
            }
        }
        stage('2. Empacotar Aplicação (.jar)') {
            steps {
                echo 'Gerando o arquivo JAR...'
                sh 'mvn package -DskipTests'
            }
        }
        stage('3. Construir Imagem Docker') {
            steps {
                echo 'Construindo a imagem Docker da API...'
                sh 'docker compose build'
            }
        }
    }
}