/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jee
 */
@Stateless
public class PaymentFacade extends AbstractFacade<Payment> {

    @PersistenceContext(unitName = "APU_Medical-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaymentFacade() {
        super(Payment.class);
    }
    
    public List<Payment> findByAppointmentId(Long appointmentId) {
    return em.createQuery("SELECT p FROM Payment p WHERE p.appointment.id = :appointmentId", Payment.class)
             .setParameter("appointmentId", appointmentId)
             .getResultList();
    }
      // All PAID payments (newest first)
    public List<Payment> findPaid() {
        return em.createQuery(
            "SELECT p FROM Payment p WHERE p.status = :st ORDER BY p.id DESC",
            Payment.class)
            .setParameter("st", "Paid")
            .getResultList();
    }

    // Any PENDING payments (if you want to show pending list)
    public List<Payment> findPending() {
        return em.createQuery(
            "SELECT p FROM Payment p WHERE p.status = :st ORDER BY p.id DESC",
            Payment.class)
            .setParameter("st", "Pending")
            .getResultList();
    }

    // Sum of today's paid collection (optional; uses your date string field)
    public double sumTodayPaid(String todayStr) {
        Number n = em.createQuery(
            "SELECT COALESCE(SUM(p.amount),0) FROM Payment p " +
            "WHERE p.status='Paid' AND p.paymentDate = :d", Number.class)
            .setParameter("d", todayStr)
            .getSingleResult();
        return n.doubleValue();
    }
    
    public double sumPaidByCustomer(Customer customer) {
        Number n = em.createQuery(
            "SELECT COALESCE(SUM(p.amount),0) FROM Payment p " +
            "WHERE p.customer = :cust AND LOWER(p.status) = 'paid'", Number.class)
            .setParameter("cust", customer)
            .getSingleResult();
        return n.doubleValue();
    }
    
}
