pipeline {
    agent any
    options { skipDefaultCheckout() }
    stages {
        stage("Build Image") {
            steps {
                script {
                    openshift.withCluster() {
                        openshift.withProject("${APP}-dev") {
                            if (!openshift.selector("bc", "${APP}").exists()) {
                                createAppBuild("${APP}", "${REPO}", "${BRANCH}");                
                            } else {
                                // Starts a new build and waits for its completion
                                openshift.selector("bc", "${APP}").startBuild("--wait=true");
                            }
                        }
                    }
                }
            }
        }
        stage("Deploy DEV") {
            steps {
                script {
                    openshift.verbose()
                    openshift.withCluster() {
                        openshift.withProject("${APP}-dev") {
                            if (!openshift.selector("dc", "${APP}").exists()) {
                                createApp("${APP}");                   
                            } else {
                                // Rollouts to latest version
                                openshift.selector("dc", "${APP}").rollout().latest();   
                            }          
                        }
                    }
                }
            }
        }
        stage("Deploy TEST") {
            steps {
                script {
                    // Asks for approval
                    input "Deploy TEST?"

                    openshift.withCluster {
                        openshift.withProject("${APP}-test") {
                            // Promotes images between environments
                            openshift.tag("${APP}-dev/hello:latest", "${APP}-test/hello:latest");

                            if (!openshift.selector("dc", "${APP}").exists()) {
                                createApp("${APP}");                   
                            } else {
                                // Rollouts to latest version
                                openshift.selector("dc", "${APP}").rollout().latest();   
                            }                            
                        }
                    }
                }
            }
        }
        stage("Deploy PROD") {
            steps {
                script {
                    // Asks for approval
                    input "Deploy PROD?"

                    openshift.withCluster {
                        openshift.withProject("${APP}-prod") {
                            // Promotes images between environments
                            openshift.tag("${APP}-test/hello:latest", "${APP}-prod/hello:latest");

                            if (!openshift.selector("dc", "${APP}").exists()) {
                                createApp("${APP}");                   
                            } else {
                                // Rollouts to latest version
                                openshift.selector("dc", "${APP}").rollout().latest();   
                            }                            
                        }
                    }
                }
            }
        }          
    }
}

def createAppBuild(app, repo, branch) {
    // BuildConfig does not exists so creates it
    def bc = openshift.newBuild("redhat-openjdk18-openshift:1.2~${repo}#${branch}", "--name=${app}", "--strategy=source").narrow("bc");
    
    // Gets the Builds related to the BuildConfig
    def builds = bc.related("builds");
    
    // Gets a map with the BuildConfig values
    def bcMap = bc.object();

    // Sets incremental builds
    bcMap.spec.strategy.sourceStrategy["incremental"] = true;
    
    // Applies the changes
    openshift.apply(bcMap);            

    // Waits for the Build completion
    builds.untilEach(1) { 
        return (it.object().status.phase == "Complete")
    }                   
}

def createApp(app) {
    // Creates the application and get the brand new BuildConfig
    openshift.newApp("${app}:latest");
    // Creates the hello Route
    openshift.selector("svc", app).expose();

    // Waits for the deployment to finish
    def latestDeploymentVersion = openshift.selector("dc", app).object().status.latestVersion
    def rc = openshift.selector("rc", "${app}-${latestDeploymentVersion}")

    rc.untilEach(1){
        def rcMap = it.object()
        return (rcMap.status.replicas.equals(rcMap.status.readyReplicas))
    }
    
    // Removes the triggers  
    openshift.set("triggers", "dc/${app}", "--manual");
}