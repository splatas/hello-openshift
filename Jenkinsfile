pipeline {
    agent {}
    stages {
        stage('Init') {
            when {
                expression {
                    openshift.withCluster() {
                        return !openshift.selector('project', 'hello-dev').exists();
                    }
                }
            }
            steps {
                script {
                    openshift.withCluster() {
                        openshift.newProject('hello-dev');
                    }
                }
            }    
        }
    }
}