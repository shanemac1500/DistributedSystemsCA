package com.fitnesscentre.DAO;

import java.util.List;
import javax.persistence.*;
import com.fitnesscentre.model.Member;
import com.fitnesscentre.model.MembershipPlan;

public class MemberDAO {

    protected static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("FitnessPU");

    public void persist(Member m) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // If JSON sent only plan.id, attach a managed reference
        if (m.getPlan() != null && m.getPlan().getId() != 0) {
            MembershipPlan ref = em.getReference(MembershipPlan.class, m.getPlan().getId());
            m.setPlan(ref);
        }

        em.persist(m);
        em.getTransaction().commit();
        em.close();
    }

    public List<Member> findAll() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Member> out = em.createQuery("from Member", Member.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return out;
    }
    
    public Member findById(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Member m = em.find(Member.class, id);
        em.getTransaction().commit();
        em.close();
        return m;
    }
}