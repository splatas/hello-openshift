package io.veicot.openshift.rest;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelloTest {

    @Test
    public void testHello() {
        Hello hello = new Hello();

        assertEquals("Hello from OpenShift!", hello.sayHello());
    }

}