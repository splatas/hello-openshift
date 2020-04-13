container("python") {
    sh "pip install requests"
    sh "python ./openshift/environments/test/integration-test/int-test.py"
}