package ca2.greenhouse.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // this marks the class as a JPA entity (a table in the DB)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    // auto-increment primary key for each user
    private int id;

    // username the person will use to log in
    private String username;

    // plain text password (fine for this CA since no hashing needed)
    private String password;   

    public User() {
        // empty constructor needed by Hibernate
    }

    // quick constructor used if we want to manually create a user object
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters for the fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}