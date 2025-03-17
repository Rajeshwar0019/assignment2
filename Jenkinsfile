pipeline {
    agent any

    environment {
        REGISTRY = "2022bcd0019" // Docker Hub or private registry username
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
                }
            }
        }

        stage('Build Order Service') {
            steps {
                dir('assignment2/order-service') {
                    bat "mvn clean package"
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def sonarHome = tool name: 'Your_SonarQube_Scanner_Name', type: 'hudson.plugins.sonar.SonarRunnerInstallation'

                    withSonarQubeEnv('SonarQube') {
                        try {
                            dir('assignment2/user-service') {
                                bat "\"${sonarHome}\\bin\\sonar-scanner\" -Dsonar.projectKey=assignment2 -Dsonar.sources=. -Dsonar.login=%SONAR_AUTH_TOKEN%"
                            }
                            dir('assignment2/order-service') {
                                bat "mvn sonar:sonar -Dsonar.projectKey=order-service -Dsonar.login=%SONAR_AUTH_TOKEN%"
                            }
                        } catch (Exception e) {
                            echo "SonarQube Analysis Failed: ${e}"
                            currentBuild.result = 'UNSTABLE'
                        }
                    }
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    try {
                        docker.build("${REGISTRY}/assignment2/user-service:${TAG}", "assignment2/user-service")
                        docker.build("${REGISTRY}/assignment2/order-service:${TAG}", "assignment2/order-service")
                    } catch (Exception e) {
                        echo "Docker Build Failed: ${e}"
                        currentBuild.result = 'FAILURE'
                        error("Stopping Pipeline due to Docker build failure")
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    try {
                        bat "docker rm -f user-service || echo 'No existing user-service container'"
                        bat "docker rm -f order-service || echo 'No existing order-service container'"
                        bat "docker run -d -p 7101:7001 --name user-service ${REGISTRY}/assignment2/user-service:${TAG}"
                        bat "docker run -d -p 7102:7002 --name order-service ${REGISTRY}/assignment2/order-service:${TAG}"
                    } catch (Exception e) {
                        echo "Deployment Failed: ${e}"
                        currentBuild.result = 'FAILURE'
                    }
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
