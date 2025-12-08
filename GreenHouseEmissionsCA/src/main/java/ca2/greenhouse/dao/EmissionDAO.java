package ca2.greenhouse.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import ca2.greenhouse.model.Emission;

@ApplicationScoped
public class EmissionDAO {

    // EntityManager used to interact with the Emission table
    @PersistenceContext
    EntityManager em;

    // save a new emission into the database
    @Transactional
    public void save(Emission e) {
        em.persist(e);
    }

    // return all emissions from the DB
    public List<Emission> findAll() {
        return em.createQuery("SELECT e FROM Emission e", Emission.class)
                 .getResultList();
    }


 // Get by category code
 public List<Emission> findByCategoryCode(String code) {
     // normalise incoming value a 
     String trimmed = code.trim();

     return em.createQuery(
             "SELECT e FROM Emission e " +
             "WHERE TRIM(e.category.code) = :code",  // use Category.code, trim in JPQL
             Emission.class)
             .setParameter("code", trimmed)
             .getResultList();
 }

    // find a single emission by id
    public Emission findById(int id) {
        return em.find(Emission.class, id);
    }

    // update an existing emission
    @Transactional
    public Emission update(Emission e) {
        // merge returns the managed instance after updating
        return em.merge(e);
    }

    // delete an emission by id
    @Transactional
    public boolean deleteById(int id) {
        Emission existing = em.find(Emission.class, id);
        
        // return false if the record doesn't exist
        if (existing == null) {
            return false;
        }

        // remove the entity if found
        em.remove(existing);
        return true;
    }
}