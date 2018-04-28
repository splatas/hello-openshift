pipeline {
    agent any
    stages {
        stage('Init') {
            when {
                expression {
                    openshift.withCluster() {
                        return !openshift.selector('project', 'hello-test').exists();
                    }
                }
            }
            steps {
                script {
                    openshift.withCluster() {
                        openshift.newProject('hello-test');
                    }
                }
            }    
        }
    }
}