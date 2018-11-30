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
        stage("Test Code") {
            steps {
                sh "mvn test"
            }
        }
        stage("Build Image") {
            steps {
                script {
                    openshift.withCluster {
                        openshift.withProject {
                            if (!openshift.selector("bc", env.APP_NAME).exists())
                                openshift.newBuild("--image-stream=${BASE_IMAGE}", "--name=${APP_NAME}", "--binary=true");
                            
                            openshift.selector("bc", env.APP_NAME).startBuild("--from-file=${APP_ARTIFACTS_DIR}", "--wait=true");
                        }
                    }
                }
            }
        }
        stage("Deploy Application") {
            steps {
                script {
                    openshift.withCluster {
                        openshift.withProject {
                            if (!openshift.selector("dc", env.APP_NAME).exists()) {
                                openshift.newApp("${APP_NAME}:latest");
                            }    
                         
                            openshift.selector("dc", env.APP_NAME).rollout().status()
                        }
                    }
                }
            }
        }
    }
}
