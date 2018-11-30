# hello-openshift

Hello World application to demonstrate OpenShift concepts and Jenkins pipelines.

## Usage

    oc create -f hello-openshift.yaml

    oc new-app --template=hello-openshift

    --> Deploying template "hello-dev/hello-openshift" to project hello-dev

        * With parameters:
            * Application Name=hello-openshift
            * Git Repository=https://github.com/leandroberetta/hello-openshift.git
            * Git Branch=develop

    --> Creating resources ...
        buildconfig "hello-openshift-pipeline" created
        imagestream "hello-openshift" created
        buildconfig "hello-openshift" created
        deploymentconfig "hello-openshift" created
        service "hello-openshift" created
        route "hello-openshift" created
    --> Success
        Use 'oc start-build hello-openshift-pipeline' to start a build.
        Use 'oc start-build hello-openshift' to start a build.
        Access your application via route 'hello-openshift-hello-dev.192.168.64.112.nip.io'
        Run 'oc status' to view your app.

## Overview

![Pipeline](src/main/resources/hello-openshift.png)

