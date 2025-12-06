package ca2.greenhouse.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import ca2.greenhouse.model.Emission;

@ApplicationScoped
public class EmissionDAO {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void save(Emission e) {
        em.persist(e);
    }

    // Get all emissions from the DB
    public List<Emission> findAll() {
        return em.createQuery("SELECT e FROM Emission e", Emission.class)
                 .getResultList();
    }

    // Get all emissions for a given category code
    public List<Emission> findByCategoryCode(String code) {
        return em.createQuery(
                    "SELECT e FROM Emission e WHERE e.categoryCode = :code",
                    Emission.class)
                 .setParameter("code", code)
                 .getResultList();
    }
}