package com.fitnesscentre.DAO;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.fitnesscentre.model.Payment;

public class PaymentDAO {

    protected static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("FitnessPU");

    public PaymentDAO() {}

    // INSERT
    public void persist(Payment payment) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(payment);
        em.getTransaction().commit();
        em.close();
    }

    // SELECT * FROM Payment
    public List<Payment> findAll() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Payment> list = em.createQuery("from Payment", Payment.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return list;
    }

    // SELECT * FROM Payment WHERE member.id = ?
    public List<Payment> findByMemberId(int memberId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Payment> allPayments = em.createQuery("from Payment", Payment.class).getResultList();
        em.getTransaction().commit();
        em.close();

        
        java.util.ArrayList<Payment> results = new java.util.ArrayList<>();
        for (Payment p : allPayments) {
            if (p.getMember().getId() == memberId) {
                results.add(p);
            }
        }
        return results;
    }

    // Calculate total payments for a member 
    public double totalPaidByMember(int memberId) {
        List<Payment> payments = findAll();
        double total = 0;
        for (Payment p : payments) {
            if (p.getMember().getId() == memberId) {
                total += p.getAmount();
            }
        }
        return total;
    }
}