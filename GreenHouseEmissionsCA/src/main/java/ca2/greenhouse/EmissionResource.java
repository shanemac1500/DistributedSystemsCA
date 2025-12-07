package ca2.greenhouse;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.inject.Inject;

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
<<<<<<< HEAD
import ca2.greenhouse.model.EmissionRecord;
import ca2.greenhouse.parser.EmissionXmlParser;


@Path("/emissions")
public class EmissionResource {

    @Inject
    EmissionDAO emissionDAO;
    
=======
import ca2.greenhouse.parser.EmissionJsonParser;
import ca2.greenhouse.parser.EmissionXmlParser;


// main REST resource for all emission endpoints
@Path("/emissions")
public class EmissionResource {

    // DAO for talking to the Emission table
    @Inject
    EmissionDAO emissionDAO;
    

    @Inject
    EmissionJsonParser jsonParser;
    
    // XML parser  for the projections file
>>>>>>> a1f020b (added user system, CRUD features, XML/JSON parsers refactor, and removal of EmissionRecord)
    @Inject
    EmissionXmlParser xmlParser;


<<<<<<< HEAD
    // simple test endpoint
=======
    // simple test endpoint to check if the service is running
>>>>>>> a1f020b (added user system, CRUD features, XML/JSON parsers refactor, and removal of EmissionRecord)
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

<<<<<<< HEAD
    //returns raw JSON array from the file
    @GET
    @Path("/all")
=======
    // returns the raw JSON array straight from the JSON file (no DB)
    @GET
    @Path("all")
>>>>>>> a1f020b (added user system, CRUD features, XML/JSON parsers refactor, and removal of EmissionRecord)
    @Produces(MediaType.APPLICATION_JSON)
    public String allEmissions() {
        try {
            JSONParser parser = new JSONParser();

<<<<<<< HEAD
=======
            // read JSON file from resources
>>>>>>> a1f020b (added user system, CRUD features, XML/JSON parsers refactor, and removal of EmissionRecord)
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

<<<<<<< HEAD
    //read JSON and insert into Emission table
    @GET
    @Path("/load-json")
=======
    // read JSON and insert the filtered rows into the Emission table
    @GET
    @Path("load-json")
>>>>>>> a1f020b (added user system, CRUD features, XML/JSON parsers refactor, and removal of EmissionRecord)
    @Produces(MediaType.TEXT_PLAIN)
    public String loadFromJson() {
        int inserted = 0;

<<<<<<< HEAD
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

=======
        // parse JSON into a list of Emission entities
        List<Emission> records = jsonParser.parseJsonEmissions();

        for (Emission e : records) {
            emissionDAO.save(e);
            inserted++;
        }

        return "Inserted " + inserted + " emission records from JSON.";
    }
    
    // reads XML predictions and inserts the filtered rows into the Emission table
    @GET
    @Path("load-xml")
    @Produces(MediaType.TEXT_PLAIN)
    public String loadFromXml() {

        // parse XML into a list of Emission entities
        List<Emission> records = xmlParser.parseXmlEmissions();

        int inserted = 0;

        for (Emission e : records) {
>>>>>>> a1f020b (added user system, CRUD features, XML/JSON parsers refactor, and removal of EmissionRecord)
            emissionDAO.save(e);
            inserted++;
        }

        return "XML parse complete. Filtered records (2023, WEM, value > 0): " + inserted;
    }
    
<<<<<<< HEAD
    // Return all emissions stored in the DB as JSON
    @GET
    @Path("/db/all")
=======

    // return all emissions stored in the DB as a JSON array
    @GET
    @Path("db/all")
>>>>>>> a1f020b (added user system, CRUD features, XML/JSON parsers refactor, and removal of EmissionRecord)
    @Produces(MediaType.APPLICATION_JSON)
    public String allFromDb() {
        List<Emission> list = emissionDAO.findAll();

        JSONArray arr = new JSONArray();

<<<<<<< HEAD
=======
        // manually build JSON objects so I have full control over the fields
>>>>>>> a1f020b (added user system, CRUD features, XML/JSON parsers refactor, and removal of EmissionRecord)
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

<<<<<<< HEAD
    // Return emissions filtered by category code
    @GET
    @Path("/db/category/{code}")
=======
    // return emissions filtered by category code
    @GET
    @Path("db/category/{code}")
>>>>>>> a1f020b (added user system, CRUD features, XML/JSON parsers refactor, and removal of EmissionRecord)
    @Produces(MediaType.APPLICATION_JSON)
    public String byCategory(@PathParam String code) {
        List<Emission> list = emissionDAO.findByCategoryCode(code);

        JSONArray arr = new JSONArray();

<<<<<<< HEAD
=======
        // same JSON format as /db/all but only for one category
>>>>>>> a1f020b (added user system, CRUD features, XML/JSON parsers refactor, and removal of EmissionRecord)
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
    
<<<<<<< HEAD
    
    

}
=======
    // get a single emission record by id from the DB
    @GET
    @Path("db/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@PathParam("id") int id) {

        Emission e = emissionDAO.findById(id);
        if (e == null) {
            // return 404 if the id does not exist
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Emission not found\"}")
                           .build();
        }

        // build JSON object for this one emission
        JSONObject obj = new JSONObject();
        obj.put("id", e.getId());
        obj.put("year", e.getYear());
        obj.put("scenario", e.getScenario());
        obj.put("categoryCode", e.getCategoryCode());
        obj.put("categoryDescription", e.getCategoryDescription());
        obj.put("value", e.getValue());
        obj.put("approved", e.isApproved());
        obj.put("approvedBy", e.getApprovedBy());

        return Response.ok(obj.toJSONString(), MediaType.APPLICATION_JSON).build();
    }
    
    // create a new emission (manual entry from the client)
    @POST
    @Path("db")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createEmission(Emission incoming) {

        // basic sanity check – year must be set and value has to be > 0
        if (incoming.getYear() == 0 || incoming.getValue() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Year and value must be set and value > 0")
                           .build();
        }

        // save new emission to the database
        emissionDAO.save(incoming);
        return Response.ok("Created emission with id " + incoming.getId()).build();
    }
    
    // update an existing emission
    @PUT
    @Path("db/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateEmission(@PathParam("id") int id, Emission incoming) {

        Emission existing = emissionDAO.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Emission not found: " + id)
                           .build();
        }

        // only update fields that are actually sent in the request
        // same pattern that was used in the FitnessService example
        if (incoming.getYear() != 0) existing.setYear(incoming.getYear());
        if (incoming.getScenario() != null) existing.setScenario(incoming.getScenario());
        if (incoming.getCategoryCode() != null) existing.setCategoryCode(incoming.getCategoryCode());
        if (incoming.getCategoryDescription() != null) existing.setCategoryDescription(incoming.getCategoryDescription());
        if (incoming.getValue() > 0) existing.setValue(incoming.getValue());
        if (incoming.getApprovedBy() != null) existing.setApprovedBy(incoming.getApprovedBy());

        emissionDAO.update(existing);
        return Response.ok("Emission " + id + " updated.").build();
    }
    
    // delete an emission by id
    @DELETE
    @Path("db/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteEmission(@PathParam("id") int id) {

        boolean ok = emissionDAO.deleteById(id);
        if (!ok) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Emission not found: " + id)
                           .build();
        }
        return Response.ok("Emission " + id + " deleted.").build();
    }
    
    // approve an emission and store the username of whoever approved it
    @PUT
    @Path("db/{id}/approve/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response approve(@PathParam("id") int id,
                            @PathParam("username") String username) {

        Emission existing = emissionDAO.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Emission not found: " + id)
                           .build();
        }

        existing.setApproved(true);
        existing.setApprovedBy(username);

        emissionDAO.update(existing);
        return Response.ok("Emission " + id + " approved by " + username).build();
    }
}
>>>>>>> a1f020b (added user system, CRUD features, XML/JSON parsers refactor, and removal of EmissionRecord)
