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

import java.util.List;

import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ca2.greenhouse.dao.EmissionDAO;
import ca2.greenhouse.model.Emission;
import ca2.greenhouse.parser.EmissionXmlParser;
import ca2.greenhouse.parser.EmissionJsonParser;
import ca2.greenhouse.UserAuthService;


// main REST resource for all emission endpoints
@Path("/emissions")
public class EmissionResource {
	
	@Inject
	UserAuthService authService;  // simple in-memory login state

    // DAO for talking to the Emission table
    @Inject
    EmissionDAO emissionDAO;
    
    // XML parser bean for the projections file
    @Inject
    EmissionXmlParser xmlParser;

    // JSON parser bean for the actual readings file
    @Inject
    EmissionJsonParser jsonParser;


    // simple test endpoint to check if the service is running
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    // read JSON file, parse it and insert the filtered rows into the Emission table
    @GET
    @Path("load-json")
    @Produces(MediaType.TEXT_PLAIN)
    public String loadFromJson() {
        int inserted = 0;

        // parse JSON into a list of Emission entities
        List<Emission> records = jsonParser.parseJsonEmissions();

        for (Emission e : records) {
            emissionDAO.save(e);
            inserted++;
        }

        return "Inserted " + inserted + " emission records from JSON.";
    }
    
    // read XML projections file and insert the filtered rows into the Emission table
    @GET
    @Path("load-xml")
    @Produces(MediaType.TEXT_PLAIN)
    public String loadFromXml() {

        // parse XML into a list of Emission entities
        List<Emission> records = xmlParser.parseXmlEmissions();

        int inserted = 0;

        for (Emission e : records) {
            // XML imports start off as not approved
            e.setApproved(false);
            e.setApprovedBy(null);

            emissionDAO.save(e);
            inserted++;
        }

        return "XML parse complete. Filtered records (2023, WEM, value > 0): " + inserted;
    }
    
 // Return all emissions stored in the DB as JSON
    @GET
    @Path("db/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String allFromDb() {

        // user must be logged in to view emissions
        if (!authService.isLoggedIn()) {
            return "{\"error\":\"You must be logged in to view emissions.\"}";
        }

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
    @Path("db/category/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String byCategory(@PathParam String code) {

        // user must be logged in to view by category
        if (!authService.isLoggedIn()) {
            return "{\"error\":\"You must be logged in to view emissions by category.\"}";
        }

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
    
 // Approve an emission using the currently logged-in user
    @PUT
    @Path("db/{id}/approve")   // IMPORTANT: no leading slash here
    @Produces(MediaType.TEXT_PLAIN)
    public Response approve(@PathParam("id") int id) {

        // 1. Must be logged in
        String currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to approve emissions.")
                           .build();
        }

        // 2. Find the emission
        Emission existing = emissionDAO.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Emission not found: " + id)
                           .build();
        }

        // 3. Approve + record who did it
        existing.setApproved(true);
        existing.setApprovedBy(currentUser);

        emissionDAO.update(existing);

        return Response.ok("Emission " + id + " approved by " + currentUser).build();
    }
}