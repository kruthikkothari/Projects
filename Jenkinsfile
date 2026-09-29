pipeline {

    agent any

    environment {
        DOCKER_IMAGE = "gostlock/myprojects"
    }

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/kruthikkothari/Projects.git'
            }
        }

        stage('Build') {
    tools {
        maven 'Maven'
    }

    steps {
        bat 'mvn clean package -DskipTests'
    }
}

        stage('Test') {
    tools {
        maven 'Maven'
    }

    steps {
        bat 'mvn test'
    }
}

        stage('Docker Build') {
            steps {
                bat 'docker build -t %DOCKER_IMAGE%:%BUILD_NUMBER% .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    bat '''
                        docker login -u %DOCKER_USERNAME% -p %DOCKER_PASSWORD%
                        docker push %DOCKER_IMAGE%:%BUILD_NUMBER%
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat '''
                    kubectl apply -f k8s\\deployment.yaml
                    kubectl apply -f k8s\\service.yaml

                    kubectl set image deployment/myprojects myprojects=%DOCKER_IMAGE%:%BUILD_NUMBER%

                    kubectl rollout status deployment/myprojects
                '''
            }
        }
    }
}
