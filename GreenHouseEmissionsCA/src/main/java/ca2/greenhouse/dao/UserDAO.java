package ca2.greenhouse.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import ca2.greenhouse.model.User;

@ApplicationScoped
public class UserDAO {

    // EntityManager gives access to the User table in the DB
    @PersistenceContext
    EntityManager em;

    // save a new user to the database
    @Transactional
    public void save(User u) {
        em.persist(u);
    }

    // return all users from the DB
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    // find a single user by their id
    public User findById(int id) {
        return em.find(User.class, id);
    }

    // find a user by their username (used for login + checking duplicates)
    public User findByUsername(String username) {
        // query may return multiple, so I just take the first one if it exists
        List<User> list = em.createQuery("SELECT u FROM User u WHERE u.username = :u", User.class)
                            .setParameter("u", username)
                            .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    // update an existing user
    @Transactional
    public User update(User u) {
        // merge returns the updated managed entity
        return em.merge(u);
    }

    // delete a user by id
    @Transactional
    public boolean deleteById(int id) {
        User existing = em.find(User.class, id);

        // return false if the user doesn't exist
        if (existing == null) return false;

        em.remove(existing);
        return true;
    }
}