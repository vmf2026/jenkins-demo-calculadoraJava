pipeline {
    agent any  // Jenkins puede ejecutar este pipeline en cualquier nodo/agente disponible


    stages {

        stage('Checkout') {
            steps {
                echo 'Descargando el código desde el repositorio...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Compilando el proyecto con Maven...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Ejecutando las pruebas unitarias...'
                sh 'mvn test'
            }
            post {
                always {
                    // Publica los resultados de JUnit en la interfaz de Jenkins
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Generando el .jar...'
                sh 'mvn package -DskipTests'
            }
        }

        stage('Archivar artefacto') {
            steps {
                echo 'Guardando el .jar como artefacto del build...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'OK. Pipeline completado con éxito.'
        }
        failure {
            echo 'KO. El pipeline ha fallado. Revisa los logs de la fase correspondiente.'
        }
    }
}
