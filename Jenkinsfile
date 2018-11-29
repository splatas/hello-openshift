pipeline {
    agent {
        label "maven"
    }
    options { 
        skipDefaultCheckout()
        disableConcurrentBuilds()
    }
    stages {
        stage("Checkout Code") {
            steps {
                git branch: env.GIT_BRANCH, url: env.GIT_REPO
            }
        }
        stage("Compile Code") {
            steps {
                sh "mvn package -DskipTests"
            }
        }
        stage("Test") {
            steps {
                sh "mvn test"
            }
        }
        stage("Build Image") {
            steps {
                script {
                    openshift.withCluster() {
                        openshift.withProject("${APP_NAME}-dev") {
                            openshift.selector("bc", "${APP_NAME}").startBuild("--from-file=${ARTIFACTS_DIR}", "--wait=true");
                        }
                    }
                }
            }
        }
        stage("Deploy DEV") {
            steps {
                script {
                    openshift.withCluster() {
                        openshift.withProject("${APP_NAME}-dev") {
                                // Rollouts to latest version
                                openshift.selector("dc", "${APP_NAME}").rollout().latest();
                            }          
                        }
                    }
                }
            }
        }
    }
}