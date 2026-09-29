```groovy
pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_IMAGE = "gostlock/myprojects"
        KUBECONFIG = "C:\\ProgramData\\Jenkins\\.kube\\config"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/kruthikkothari/Projects.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Docker Build') {
            steps {
                bat '''
                    docker build -t %DOCKER_IMAGE%:%BUILD_NUMBER% .
                    docker tag %DOCKER_IMAGE%:%BUILD_NUMBER% %DOCKER_IMAGE%:latest
                '''
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
                        docker push %DOCKER_IMAGE%:latest

                        docker logout
                    '''
                }
            }
        }

        stage('Test Kubernetes') {
            steps {
                bat '''
                    echo ========================================
                    echo Testing Kubernetes Connection
                    echo ========================================

                    echo KUBECONFIG=%KUBECONFIG%

                    kubectl config current-context

                    kubectl cluster-info

                    kubectl get nodes
                '''
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat '''
                    echo ========================================
                    echo Deploying to Kubernetes
                    echo ========================================

                    kubectl apply -f k8s\\deployment.yaml

                    kubectl apply -f k8s\\service.yaml

                    kubectl set image deployment/myprojects myprojects=%DOCKER_IMAGE%:%BUILD_NUMBER%

                    kubectl rollout status deployment/myprojects
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                bat '''
                    echo ========================================
                    echo Kubernetes Deployment
                    echo ========================================

                    kubectl get deployments

                    kubectl get pods

                    kubectl get services
                '''
            }
        }
    }

    post {
        success {
            echo '========================================'
            echo 'CI/CD PIPELINE COMPLETED SUCCESSFULLY'
            echo '========================================'
        }

        failure {
            echo '========================================'
            echo 'CI/CD PIPELINE FAILED'
            echo 'Check the Jenkins console output.'
            echo '========================================'
        }
    }
}
```
