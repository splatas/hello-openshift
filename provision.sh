#!/usr/bin/env bash

APP="hello"
REPO="https://github.com/leandroberetta/hello-openshift"
BRANCH="master"

oc new-project hello-dev

oc new-app $REPO --strategy=pipeline --name="hello-pipeline"

oc set env bc/hello-pipeline APP=$APP -n hello-dev
oc set env bc/hello-pipeline REPO=$REPO -n hello-dev
oc set env bc/hello-pipeline BRANCH=$BRANCH -n hello-dev

oc new-project hello-test
oc new-project hello-prod

oc policy add-role-to-user edit system:serviceaccount:hello-dev:jenkins -n hello-test
oc policy add-role-to-user edit system:serviceaccount:hello-dev:jenkins -n hello-prod

