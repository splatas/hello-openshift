package io.veicot.openshift.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

@ApplicationScoped
@Path("/hello")
public class HelloService extends Application {

	@Inject
	Hello hello;

	@GET
	@Produces("text/plain")
	public Response hello() {
		return Response.ok(hello.sayHello()).build();
	}
}