package ca2.greenhouse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Path("/emissions")
public class EmissionResource {

    // Simple test endpoint
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    // Read the local GreenhouseGasEmissions2025.json file from the classpath
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String allEmissions() {
        try {
            // 1) Load file from classpath (src/main/resources)
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("GreenhouseGasEmissions2025.json");

            if (is == null) {
                // If this happens, the file is not where Quarkus expects it
                return "{\"error\":\"GreenhouseGasEmissions2025.json not found on classpath\"}";
            }

            // 2) Wrap in a Reader so JSONParser can handle it
            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);

            // 3) Parse JSON
            Object obj = new JSONParser().parse(reader);
            JSONObject root = (JSONObject) obj;

            // 4) Get the 'Emissions' array
            JSONArray emissionsArray = (JSONArray) root.get("Emissions");

            // 5) Return that array as JSON text
            return emissionsArray.toJSONString();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }
}