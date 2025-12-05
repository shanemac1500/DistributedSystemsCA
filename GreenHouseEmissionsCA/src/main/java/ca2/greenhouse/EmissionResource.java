package ca2.greenhouse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Path("/emissions")
public class EmissionResource {

    // -------------------------------
    // Simple test endpoint
    // -------------------------------
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    // -------------------------------
    // Default endpoint (/emissions)
    // -------------------------------
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String defaultMsg() {
        return "Emissions endpoint is running";
    }

    // -------------------------------
    // Read all emissions from JSON
    // -------------------------------
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String allEmissions() {

        try {
            // Load file from src/main/resources
            InputStream is = getClass().getResourceAsStream("/GreenhouseGasEmissions2025.json");

            if (is == null) {
                return "{\"error\":\"GreenhouseGasEmissions2025.json not found\"}";
            }

            // Parse JSON
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new InputStreamReader(is));

            JSONObject root = (JSONObject) obj;

            // Get array named "Emissions"
            JSONArray arr = (JSONArray) root.get("Emissions");

            return arr.toJSONString();

        } catch (Exception e) {
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }
}