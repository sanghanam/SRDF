package edu.kaist.mrlab.srdf.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import edu.kaist.mrlab.srdf.KoSeCT;
import edu.kaist.mrlab.srdf.SRDF;
import edu.kaist.mrlab.srdf.modules.Preprocessor;
import edu.kaist.mrlab.srdf.modules.SentenceSplitter;

@Path("/srdf")
public class SRDFPOSTService {
	
	KoSeCT kosect = new KoSeCT();
	Preprocessor p = new Preprocessor();
	SentenceSplitter ss = new SentenceSplitter();

	@SuppressWarnings("unchecked")
	@POST
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	// @Produces("text/plain; charset=UTF-8")
	public Response getPost(String input) throws Exception {

		// @FormParam("text")
		
		SRDF srdf = new SRDF();
		String result = "empty";
		
		JSONParser jsonParser = new JSONParser();
		JSONObject reader = (JSONObject) jsonParser.parse(input);
		String mode = (String) reader.get("mode");
		String text = (String) reader.get("text");
		String out = (String) reader.get("out");
		
		if(mode.equals("stc")){
			result = srdf.doOneSentence(kosect, p, ss, text);
		}
		
		if(out.equals("plain")){
			
			
			
		} else if(out.equals("json")){
			
			JSONObject resultOBJ = new JSONObject();
			resultOBJ.put("triples", result);
			
			result = resultOBJ.toString();
			
		}
		
		return Response.ok(result).entity(result)
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept").build();

	}

	@OPTIONS
	@Consumes("application/x-www-form-urlencoded; charset=utf-8")
	@Produces("text/plain; charset=utf-8")
	public Response getOptions(@FormParam("text") String input) throws JSONException {

		return Response.ok().header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept").build();

	}

}
