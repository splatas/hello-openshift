package io.veicot.openshift.rest;

import javax.enterprise.context.Dependent;

@Dependent
public class Hello {

    public String sayHello() {
        return "Hello from OpenShift!";
    }
}
