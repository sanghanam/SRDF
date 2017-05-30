package edu.kaist.mrlab.srdf.rest;

import java.util.ArrayList;

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
import edu.kaist.mrlab.srdf.data.Chunk;
import edu.kaist.mrlab.srdf.modules.Chunker;
import edu.kaist.mrlab.srdf.modules.EntityLinker;
import edu.kaist.mrlab.srdf.modules.Identifier;
import edu.kaist.mrlab.srdf.modules.Preprocessor;
import edu.kaist.mrlab.srdf.modules.SRExtractor;
import edu.kaist.mrlab.srdf.modules.SentenceSplitter;

@Path("/sr_srdf")
public class SRSRDFPOSTService {

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

		SRExtractor sre = new SRExtractor();
		KoSeCT kosect = new KoSeCT();
		Preprocessor p = new Preprocessor();
		Identifier iden = new Identifier();
		
		String result = "empty";

		JSONParser jsonParser = new JSONParser();
		JSONObject reader = (JSONObject) jsonParser.parse(input);
		String text = (String) reader.get("text");

		input = p.changeSymbol(text);
		input = kosect.removeUTF8BOM(input);
		input = p.removeBracket(input);
		
		ArrayList<Chunker> chunkers = kosect.doPreprocessWithoutSplitting(input);

		if (!p.isValidChunk(chunkers) || chunkers.isEmpty()) {
			System.exit(0);;
		}
		
		String triples = null;
		
		for (Chunker c : chunkers) {
			iden.identifyVP(c.getVPChunks());
			
			ArrayList<Chunk> NPChunks = c.getNPChunks();
			ArrayList<Chunk> VPChunks = c.getNPChunks();
			
			triples = sre.makeSRTriple(NPChunks, VPChunks);
			System.out.println(triples);
			
		}
		
		JSONObject resultOBJ = new JSONObject();
		resultOBJ.put("triples", triples);

		result = resultOBJ.toString();

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
