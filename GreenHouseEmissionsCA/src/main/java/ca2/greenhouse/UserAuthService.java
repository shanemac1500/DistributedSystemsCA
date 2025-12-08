package ca2.greenhouse;

import javax.enterprise.context.ApplicationScoped;

/**
 *  authentication helper.
 * Stores the username of the currently "logged in" user in memory.
 */
@ApplicationScoped
public class UserAuthService {

    private String currentUser;

    // called after a successful login
    public void login(String username) {
        this.currentUser = username;
    }

    // called to log out the current user
    public void logout() {
        this.currentUser = null;
    }

    // check if someone is logged in
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // get the username of the logged-in user
    public String getCurrentUser() {
        return currentUser;
    }
}