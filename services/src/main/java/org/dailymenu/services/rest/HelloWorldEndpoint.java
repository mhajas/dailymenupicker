package org.dailymenu.services.rest;


import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * For testing only. Will be removed.
 */
@Path("/hello")
public class HelloWorldEndpoint {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "Hello RESTEasy";
	}
}
