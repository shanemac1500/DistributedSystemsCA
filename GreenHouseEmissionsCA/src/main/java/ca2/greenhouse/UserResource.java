package ca2.greenhouse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca2.greenhouse.dao.UserDAO;
import ca2.greenhouse.model.User;

import java.util.List;

// REST resource for user registration, login and basic user CRUD
@Path("/users")
public class UserResource {

    // DAO for talking to the User table
    @Inject
    UserDAO userDAO;

    // register a new user
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response register(User user) {
        // basic check to make sure both fields are filled in
        if (user.getUsername() == null || user.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Username and password required.")
                           .build();
        }

        // check if the username is already in the database
        if (userDAO.findByUsername(user.getUsername()) != null) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("Username already taken.")
                           .build();
        }

        // save the new user
        userDAO.save(user);
        return Response.ok("User registered: " + user.getUsername()).build();
    }

    // login endpoint – very simple version just checking username and password
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(User incoming) {

        // find user by username
        User u = userDAO.findByUsername(incoming.getUsername());
        // if user does not exist or password is wrong, reject login
        if (u == null || !u.getPassword().equals(incoming.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("Invalid username or password.")
                           .build();
        }

        // return a message
        return Response.ok("Login successful for " + u.getUsername()).build();
    }

    // return all users from the database
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> allUsers() {
        return userDAO.findAll();
    }

    // get one user by id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@PathParam("id") int id) {
        User u = userDAO.findById(id);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"User not found\"}")
                           .build();
        }
        return Response.ok(u).build();
    }

    // update an existing user
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response update(@PathParam("id") int id, User incoming) {
        User u = userDAO.findById(id);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found.")
                           .build();
        }

        // only update the fields that were sent in the request
        if (incoming.getUsername() != null) u.setUsername(incoming.getUsername());
        if (incoming.getPassword() != null) u.setPassword(incoming.getPassword());

        userDAO.update(u);
        return Response.ok("User updated.").build();
    }

    // delete a user by id
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(@PathParam("id") int id) {
        boolean ok = userDAO.deleteById(id);
        if (!ok) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found.")
                           .build();
        }
        return Response.ok("User deleted.").build();
    }
}