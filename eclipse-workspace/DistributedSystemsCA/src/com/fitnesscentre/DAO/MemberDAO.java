package com.fitnesscentre.DAO;

import java.util.List;
import javax.persistence.*;
import com.fitnesscentre.model.Member;
import com.fitnesscentre.model.MembershipPlan;

public class MemberDAO {

    protected static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("FitnessPU");
    
    //Inserts a new member into the db
    //If a plan is provided with only an id, it will attach a managed ref
    public void persist(Member m) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // If JSON sent only plan.id, attach a managed reference
        if (m.getPlan() != null && m.getPlan().getId() != 0) {
            MembershipPlan ref = em.getReference(MembershipPlan.class, m.getPlan().getId());
            m.setPlan(ref);
        }

        em.persist(m);//save new member
        em.getTransaction().commit();
        em.close();
    }
    
    //Get all members from db
    public List<Member> findAll() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Member> out = em.createQuery("from Member", Member.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return out;
    }
    
    //Find a member by the primary key (id).
    public Member findById(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Member m = em.find(Member.class, id);
        em.getTransaction().commit();
        em.close();
        return m;
    }
    
    //Update existing member
    public Member merge(Member m) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Member managed = em.merge(m);   
        em.getTransaction().commit();
        em.close();
        return managed;
    }
    
    //Delete a member by id
    //Returns true if deletion is successful, false if the member was not found
    public boolean deleteById(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Member managed = em.find(Member.class, id);
        if (managed == null) {
            em.getTransaction().rollback();
            em.close();
            return false;
        }
        em.remove(managed);
        em.getTransaction().commit();
        em.close();
        return true;
    }
    
    
}