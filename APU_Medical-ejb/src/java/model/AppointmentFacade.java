/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jee
 */
@Stateless
public class AppointmentFacade extends AbstractFacade<Appointment> {

    @PersistenceContext(unitName = "APU_Medical-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AppointmentFacade() {
        super(Appointment.class);
    }
    
    public long countByCustomerId(String customerId) {
    return em.createQuery(
          "SELECT COUNT(a) FROM Appointment a WHERE a.customer.id = :customerId", Long.class)
          .setParameter("customerId", customerId)
          .getSingleResult();
    }
   
    public long countUpcomingByCustomerId(String customerId) {
    return em.createQuery(
        "SELECT COUNT(a) FROM Appointment a WHERE a.customer.id = :customerId AND " +
        "(a.date > CURRENT_DATE OR (a.date = CURRENT_DATE AND a.time > :nowTime))", Long.class)
        .setParameter("customerId", customerId)
        .setParameter("nowTime", java.time.LocalTime.now().toString())
        .getSingleResult();
} 
    
//    public String generateAppointmentId() {
//    List<Appointment> list = em.createQuery("SELECT a FROM Appointment a ORDER BY a.id DESC", Appointment.class).setMaxResults(1).getResultList();
//    if (list.isEmpty()) {
//        return "Appoint-001";
//        }
//        String lastId = list.get(0).getId(); // e.g., "Appoint-007"
//        int num = Integer.parseInt(lastId.substring(8)) + 1;
//        return String.format("Appoint-%03d", num);
//    }
    
//    public List<Appointment> findTop2UpcomingByCustomerId(String customerId) {
//    return em.createQuery(
//        "SELECT a FROM Appointment a WHERE a.customer.id = :customerId AND " +
//        "(a.date > CURRENT_DATE OR (a.date = CURRENT_DATE AND a.time > :nowTime)) " +
//        "ORDER BY a.date ASC, a.time ASC", Appointment.class)
//        .setParameter("customerId", customerId)
//        .setParameter("nowTime", java.time.LocalTime.now().toString()) // assuming `a.time` is a String like "14:00"
//        .setMaxResults(2)
//        .getResultList();
//    }
   
    public List<Appointment> findByCustomer(Customer customer) {
        return em.createQuery("SELECT a FROM Appointment a WHERE a.customer = :customer", Appointment.class)
                 .setParameter("customer", customer)
                 .getResultList();
    }
    
    // Dashboard query methods
    public long countTodaysAppointments() {
        return em.createQuery(
            "SELECT COUNT(a) FROM Appointment a WHERE a.date = :today", Long.class)
            .setParameter("today", java.time.LocalDate.now().toString())
            .getSingleResult();
    }
    
    public long countPendingAppointments() {
        return em.createQuery(
            "SELECT COUNT(a) FROM Appointment a WHERE a.status = 'Pending'", Long.class)
            .getSingleResult();
    }
    
    public Double getMonthlyRevenue() {
        try {
            String currentMonth = java.time.LocalDate.now().toString().substring(0, 7); // YYYY-MM format
            Object result = em.createQuery(
                "SELECT SUM(CAST(a.payment AS double)) FROM Appointment a WHERE a.date LIKE :month AND a.payment IS NOT NULL")
                .setParameter("month", currentMonth + "%")
                .getSingleResult();
            return result != null ? (Double) result : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
    
}

