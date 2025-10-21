package com.fitnesscentre.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/fitness")
public class FitnessService {
	
	@GET
	@Path("/hello")
	@Produces("text/plain")
	public String hello() {
		return "Hello from FitnessService!";
	}
	

}
