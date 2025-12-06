package ca2.greenhouse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ca2.greenhouse.dao.EmissionDAO;
import ca2.greenhouse.model.Emission;
import ca2.greenhouse.model.EmissionRecord;
import ca2.greenhouse.parser.EmissionXmlParser;


@Path("/emissions")
public class EmissionResource {

    @Inject
    EmissionDAO emissionDAO;
    
    @Inject
    EmissionXmlParser xmlParser;


    // simple test endpoint
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    //returns raw JSON array from the file
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String allEmissions() {
        try {
            JSONParser parser = new JSONParser();

            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("GreenhouseGasEmissions2025.json");
            if (is == null) {
                return "{\"error\":\"GreenhouseGasEmissions2025.json not found on classpath\"}";
            }

            JSONObject root = (JSONObject) parser.parse(new InputStreamReader(is));
            JSONArray emissions = (JSONArray) root.get("Emissions");

            return emissions.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }

    //read JSON and insert into Emission table
    @GET
    @Path("/load-json")
    @Produces(MediaType.TEXT_PLAIN)
    public String loadFromJson() {
        int inserted = 0;

        try {
            JSONParser parser = new JSONParser();

            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("GreenhouseGasEmissions2025.json");
            if (is == null) {
                return "GreenhouseGasEmissions2025.json not found on classpath";
            }

            JSONObject root = (JSONObject) parser.parse(new InputStreamReader(is));
            JSONArray emissions = (JSONArray) root.get("Emissions");

            for (Object o : emissions) {
                JSONObject row = (JSONObject) o;

                // only keep values > 0
                Number valNumber = (Number) row.get("Value");
                if (valNumber == null) {
                    continue;
                }
                double value = valNumber.doubleValue();
                if (value <= 0) {
                    continue;
                }

                String category = (String) row.get("Category");
                String gasUnits = (String) row.get("Gas Units");

                Emission e = new Emission();
                // JSON file is for 2023
                e.setYear(2023);
               
                
                e.setScenario("Actual 2023");
                e.setCategoryCode(category);
                // temporary 
                e.setCategoryDescription(gasUnits);
                e.setValue(value);
                e.setApproved(false);
                e.setApprovedBy(null);

                emissionDAO.save(e);
                inserted++;
            }

            return "Inserted " + inserted + " emission records from JSON.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while loading JSON: " + e.getMessage();
        }
    }
    
 //reads XML predictions and insert filtered rows into Emission table
    @GET
    @Path("/load-xml")
    @Produces(MediaType.TEXT_PLAIN)
    public String loadFromXml() {

        // Parse XML rows (Year, Scenario, Category__1_3, Gas___Units, Value...)
        java.util.List<EmissionRecord> records = xmlParser.parseXmlEmissions();

        int inserted = 0;

        for (EmissionRecord rec : records) {

            //  Emission entity
            Emission e = new Emission();
            e.setYear(rec.getYear());
            e.setScenario(rec.getScenario());              
            e.setCategoryCode(rec.getCategoryCode());      // Category__1_3
            e.setCategoryDescription(rec.getCategoryDescription()); // Gas___Units
            e.setValue(rec.getValue());

            // start as not approved
            e.setApproved(false);
            e.setApprovedBy(null);

            emissionDAO.save(e);
            inserted++;
        }

        return "XML parse complete. Filtered records (2023, WEM, value > 0): " + inserted;
    }
    
    // Return all emissions stored in the DB as JSON
    @GET
    @Path("/db/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String allFromDb() {
        List<Emission> list = emissionDAO.findAll();

        JSONArray arr = new JSONArray();

        for (Emission e : list) {
            JSONObject obj = new JSONObject();
            obj.put("id", e.getId());
            obj.put("year", e.getYear());
            obj.put("scenario", e.getScenario());
            obj.put("categoryCode", e.getCategoryCode());
            obj.put("categoryDescription", e.getCategoryDescription());
            obj.put("value", e.getValue());
            obj.put("approved", e.isApproved());
            obj.put("approvedBy", e.getApprovedBy());
            arr.add(obj);
        }

        return arr.toJSONString();
    }

    // Return emissions filtered by category code
    @GET
    @Path("/db/category/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String byCategory(@PathParam String code) {
        List<Emission> list = emissionDAO.findByCategoryCode(code);

        JSONArray arr = new JSONArray();

        for (Emission e : list) {
            JSONObject obj = new JSONObject();
            obj.put("id", e.getId());
            obj.put("year", e.getYear());
            obj.put("scenario", e.getScenario());
            obj.put("categoryCode", e.getCategoryCode());
            obj.put("categoryDescription", e.getCategoryDescription());
            obj.put("value", e.getValue());
            obj.put("approved", e.isApproved());
            obj.put("approvedBy", e.getApprovedBy());
            arr.add(obj);
        }

        return arr.toJSONString();
    }
    
    
    

}
