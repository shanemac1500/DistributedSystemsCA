package ca2.greenhouse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca2.greenhouse.dao.UserDAO;
import ca2.greenhouse.model.User;

import java.util.List;

@Path("/users")
public class UserResource {

    @Inject
    UserDAO userDAO;

    @Inject
    UserAuthService authService;

    // Register a new user
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response register(User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Username and password required.")
                           .build();
        }

        if (userDAO.findByUsername(user.getUsername()) != null) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("Username already taken.")
                           .build();
        }

        userDAO.save(user);
        return Response.ok("User registered: " + user.getUsername()).build();
    }

    // Login
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(User incoming) {

        User u = userDAO.findByUsername(incoming.getUsername());
        if (u == null || !u.getPassword().equals(incoming.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("Invalid username or password.")
                           .build();
        }

        // marks this user as logged in in memory
        authService.login(u.getUsername());

        return Response.ok("Login successful for " + u.getUsername()).build();
    }

    // Logout –clears the in-memory user
    @POST
    @Path("/logout")
    @Produces(MediaType.TEXT_PLAIN)
    public Response logout() {
        authService.logout();
        return Response.ok("Logged out.").build();
    }

    // View all users
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> allUsers() {
        return userDAO.findAll();
    }

    // Get one user
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

    // Update user
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
        if (incoming.getUsername() != null) u.setUsername(incoming.getUsername());
        if (incoming.getPassword() != null) u.setPassword(incoming.getPassword());

        userDAO.update(u);
        return Response.ok("User updated.").build();
    }

    // Delete user
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