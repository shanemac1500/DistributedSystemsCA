package com.fitnesscentre.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.fitnesscentre.model.MembershipPlan;

public class MembershipPlanDAO {
	
	protected static EntityManagerFactory emf = 
			Persistence.createEntityManagerFactory("FitnessPU");
	
	public MembershipPlanDAO() {}
	//CRUD
	
	//persist (INSERT)
	public void persist(MembershipPlan plan) {
		EntityManager em =emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(plan);
		em.getTransaction().commit();
		em.close();
	}
	
	//remove (DELETE)
	//Merge first incase the entity is detached
	public void remove(MembershipPlan plan) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.remove(em.merge(plan));
		em.getTransaction().commit();
		em.close();
	}
	
	//merge (UPDATE)
	//returns the updated instance
	public MembershipPlan merge(MembershipPlan plan) {
		EntityManager em =emf.createEntityManager();
		em.getTransaction().begin();
		MembershipPlan updated = em.merge(plan);
		em.getTransaction().commit();
		em.close();
		return updated;
	}
	
	//Queries
	
	//find all (SELECT * FROM MembershipPlan)
	public List<MembershipPlan> findAll() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<MembershipPlan> plans = new ArrayList<>();
        plans = em.createQuery("from MembershipPlan", MembershipPlan.class)
                  .getResultList();
        em.getTransaction().commit();
        em.close();
        return plans;
    }
	
	//find by primary key
	public MembershipPlan findById(int id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		MembershipPlan plan = em.find(MembershipPlan.class, id);
		em.getTransaction().commit();
		em.close();
		return plan;
		
	}
	
	

}
