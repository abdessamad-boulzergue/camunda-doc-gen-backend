pipeline {
    agent {
        kubernetes {
            yaml '''
                apiVersion: v1
                kind: Pod
                metadata:
                labels:
                    app: camunda-doc-gen-backend-build
                spec:
                    containers:
                    - name: maven
                      image: maven:3.9.6-eclipse-temurin-21
                      command:
                      - cat
                      tty: true
                    - name: docker
                      image: docker:24-dind
                      securityContext:
                        privileged: true
                      env:
                      - name: DOCKER_TLS_CERTDIR
                        value: ""
                      volumeMounts:
                      - name: docker-config
                        mountPath: /root/.docker/
                      command:
                      - dockerd-entrypoint.sh
                      tty: true
                    volumes:
                    - name: docker-config
                      secret:
                        secretName: dockerhub-secret
                        items:
                        - key: .dockerconfigjson
                          path: config.json
                '''
        }
    }

    environment {
        DOCKER_IMAGE = 'camunda-doc-gen-backend'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        DOCKER_REPO = 'abdosblz/camunda-doc-gen-backend'
    }

    stages {
        stage('Build Maven') {
            steps {
                container('maven') {
                    script {
                        def projectVersion = sh(script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
                        env.DOCKER_TAG = "${projectVersion}-v${BUILD_NUMBER}"
                        echo "Docker Tag: ${env.DOCKER_TAG}"
                    }
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                container('docker') {
                    script {
                        // Wait/Check for docker socket? Usually dind is ready fast enough.
                        sh "docker build -t ${DOCKER_REPO}:${DOCKER_TAG} ."
                        sh "docker tag ${DOCKER_REPO}:${DOCKER_TAG} ${DOCKER_REPO}:latest"
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                container('docker') {
                    script {
                        sh "docker push ${DOCKER_REPO}:${DOCKER_TAG}"
                        sh "docker push ${DOCKER_REPO}:latest"
                    }
                }
            }
        }

        stage('Update GitOps') {
          steps {
            withCredentials([usernamePassword(
              credentialsId: 'github-repo',
              usernameVariable: 'GIT_USERNAME',
              passwordVariable: 'GIT_TOKEN'
            )]) {
              sh """
                git clone https://${GIT_USERNAME}:${GIT_TOKEN}@github.com/${GIT_USERNAME}/camunda-doc-argocd.git
                cd camunda-doc-argocd/camunda-doc-gen-backend

                sed -i "s|image: .*|image: docker.io/${DOCKER_REPO}:${DOCKER_TAG}|g" deployment.yaml

                git config user.name ${GIT_USERNAME}
                git config user.email "jenkins@example.com"

                git commit -am "Update image to ${DOCKER_TAG}"
                git push
              """
            }
          }
        }
    }
}

