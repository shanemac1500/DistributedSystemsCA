package ca2.greenhouse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ca2.greenhouse.dao.CategoryDAO;

// REST endpoint for working with categories
@Path("/categories")
public class CategoryResource {

    @Inject
    CategoryDAO categoryDAO;   // DAO used to read/write category data

    // Loads category code + description pairs from the EFDB text file
    @GET
    @Path("/load")
    @Produces(MediaType.TEXT_PLAIN)
    public String loadCategories() {
        int count = categoryDAO.loadFromFile();   // returns how many were added/updated
        return "Loaded " + count + " categories from EFDB file.";
    }
    
    // checks how many categories are currently stored in the DB
    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countCategories() {
        long count = categoryDAO.countAll();
        return "Category count in JPA: " + count;
    }
}