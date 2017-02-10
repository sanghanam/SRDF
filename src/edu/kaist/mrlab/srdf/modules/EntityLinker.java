package edu.kaist.mrlab.srdf.modules;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.kaist.mrlab.srdf.data.Entity;

public class EntityLinker {

	BufferedReader filebr;
	ArrayList<Entity> entities = new ArrayList<Entity>();

	public String changeURI(String srdfOutput) {

		String elLinkedOutput = "";

		try {

			// filebr = new BufferedReader(
			// new InputStreamReader(new
			// FileInputStream("data/test/srdf_output.txt"), "UTF8"));

			StringTokenizer stLine = new StringTokenizer(srdfOutput, "\n");

			while (stLine.hasMoreTokens()) {
				String input = stLine.nextToken();
				if (input.length() != 0) {
					// readedStc++;

					StringTokenizer st = new StringTokenizer(input, "\t");
					String subject = st.nextToken();
					String predicate = st.nextToken();
					String object = st.nextToken();

					for (int i = 0; i < entities.size(); i++) {

						String entityText = entities.get(i).getText();
						if (subject.equals(entityText))
							subject = entities.get(i).getUri();
						if (object.equals(entityText))
							object = entities.get(i).getUri();

					}

					elLinkedOutput += subject + "\t" + predicate + "\t" + object + "\n";
//					System.out.println(subject + "\t" + predicate + "\t" + object);

				}

			}

			// filebr.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return elLinkedOutput;

	}

	public String callEL(String inputText) {

		String output = null;

		try {

			Client client = Client.create();

			WebResource webResource = client.resource("http://elvis.kaist.ac.kr:2223/entity_linking");

			String input = "{\"text\": \"" + inputText + "\"}";

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			// System.out.println("Output from Server .... \n");
			output = response.getEntity(String.class);
			// System.out.println(output);

		} catch (Exception e) {

			e.printStackTrace();

		}
		return output;

	}

	public void parseELOutput(String output) {

		try {

			JSONParser parser = new JSONParser();

			Object obj = parser.parse(output);

			JSONArray jsonArray = (JSONArray) obj;
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {

				JSONObject entity = iterator.next();
				String text = (String) entity.get("text");
				String uri = (String) entity.get("uri");
				int start = Integer.parseInt(entity.get("start_offset").toString());
				int end = Integer.parseInt(entity.get("end_offset").toString());

				entities.add(new Entity(text, uri, start, end));

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String pipeEL(String srdfOutput, String inputText) {

		EntityLinker el = new EntityLinker();
		String elOutput = el.callEL(inputText);
		el.parseELOutput(elOutput);
		String result = el.changeURI(srdfOutput);
		return result;
	}

	public static void main(String[] ar) {

		EntityLinker el = new EntityLinker();
		String elOutput = el.callEL("정약용은 화성을 쌓기 위해 기기도설이란 책을 참고하였다.");
		el.parseELOutput(elOutput);
		// el.changeURI("정약용은 화성을 쌓기 위해 기기도설이란 책을 참고하였다.");

	}
}
