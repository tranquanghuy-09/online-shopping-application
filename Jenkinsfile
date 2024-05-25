pipeline {
    agent any

    tools {
        jdk 'jdk17'
        gradle 'gradle'
    }

    environment {
        DOCKER_REGISTRY_URL = 'docker.io'
    }

    stages {
        stage('Git Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/tranquanghuy-09/online-shopping-application']])
            }
        }
        stage('Build and Test All Services') {
            steps {
                script {
                    def services = [
                        'DiscoveryService',
                        'CustomerService',
                        'EmployeeService',
                        'AuthService',
                        'InventoryService',
                        'OrderService',
                        'ProductService',
                        'ApiGateway',
                    ]
                    for (service in services) {
                        dir(service) {
                            sh "${tool name: 'gradle', type: 'gradle'}/bin/gradle clean build"
                        }
                    }
                }
            }
        }
        stage('Build Docker images with Kaniko') {
            steps {
                script {
                    def services = [
                        'DiscoveryService',
                        'CustomerService',
                        'EmployeeService',
                        'AuthService',
                        'InventoryService',
                        'OrderService',
                        'ProductService',
                        'ApiGateway',
                    ]
                    for (service in services) {
                        dir(service) {
                            sh """
                            echo quanghuy09 | docker login -u tranquanghuyiy09 --password-stdin ${DOCKER_REGISTRY_URL}
                            docker run \
                                -v \$(pwd):/workspace \
                                -v /Users/tranquanghuyit09/kaniko/.docker:/kaniko/.docker \
                                gcr.io/kaniko-project/executor:latest \
                                --dockerfile /workspace/Dockerfile \
                                --destination=${DOCKER_REGISTRY_URL}/tranquanghuyiy09/${service.toLowerCase()}:latest \
                                --context /workspace/
                            """
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build and Test completed successfully for all services!'
            emailext(
                subject: "Build Success: ${currentBuild.fullDisplayName}",
                body: """Build and Test completed successfully for all services! 
Successfully pushed all images to Docker Hub.
Congratulations on your 10 points!
                        
Translate: Chúc mừng bạn Huy được 10 điểm nhen!
                        """,
                to: 'tranquanghuyit09@gmail.com'
            )
        }
        failure {
            echo 'Build or Test failed for one or more services.'
            emailext(
                subject: "Build Failed: ${currentBuild.fullDisplayName}",
                body: "Build or Test failed for one or many.",
                to: 'tranquanghuyit09@gmail.com'
            )
        }
    }
}
