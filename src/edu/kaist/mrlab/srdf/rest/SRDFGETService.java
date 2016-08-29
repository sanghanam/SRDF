package edu.kaist.mrlab.srdf.rest;

import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.JSONException;

@Path("/example/service")
public class SRDFGETService {

	@GET
	public Response getSRDF(@PathParam("text") String input) throws JSONException {
		
		String result = input;

		return Response.ok().entity(result).header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
				.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "*").build();

	}

}
