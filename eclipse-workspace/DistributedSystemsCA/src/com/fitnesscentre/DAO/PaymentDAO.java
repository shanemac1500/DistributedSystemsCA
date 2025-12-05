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

    // Inserts a new payment into db
    //
    public void persist(Payment payment) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(payment);
        em.getTransaction().commit();
        em.close();
    }

    // Gets all payments from db
    //SELECT * FROM Payment
    // @return a list of all payments
    public List<Payment> findAll() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Payment> list = em.createQuery("from Payment", Payment.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return list;
    }

    // Finds all payments made from a specific member
    public List<Payment> findByMemberId(int memberId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Payment> allPayments = em.createQuery("from Payment", Payment.class).getResultList();
        em.getTransaction().commit();
        em.close();

        //filter manually by member ID
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