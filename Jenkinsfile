pipeline {
    agent any

    environment {
        REGISTRY = "2022bcd0019" // Change to your Docker Hub or registry username
        TAG = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Rajeshwar0019/assignment2.git'
            }
        }
        stage('Build User Service') {
            steps {
                dir('assignment2/user-service') {
                    nodejs('nodejs') {
                    bat "npm install"
                    bat "npm test"
                }

                    // You could add a build step if needed
                }
            }
        }
        stage('Build Order Service') {
            steps {
                dir('assignment2/order-service') {
                    def mvnHome = tool 'Maven3'
                     bat "\"${mvnHome}\\bin\\mvn\" clean package"
                }
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    // Build Docker images for each service
                    docker.build("${REGISTRY}/assignment2/user-service:${TAG}", "user-service")
                    docker.build("${REGISTRY}/assignment2/order-service:${TAG}", "order-service")
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // Remove existing containers if they exist
                    bat "docker rm -f user-service || echo 'No existing user-service container'"
                    bat "docker rm -f order-service || echo 'No existing order-service container'"

                    // Run user-service: Map container port 7001 to host port 7101
                    bat "docker run -d -p 7101:7001 --name user-service ${REGISTRY}/user-service:${TAG}"

                    // Run order-service: Map container port 7002 to host port 7102
                    bat "docker run -d -p 7102:7002 --name order-service ${REGISTRY}/order-service:${TAG}"
                }
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: '**/target/*.jar, **/build/**/*', fingerprint: true
        }
    }
}
