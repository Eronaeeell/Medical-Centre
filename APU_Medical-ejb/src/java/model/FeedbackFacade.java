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
public class FeedbackFacade extends AbstractFacade<Feedback> {

    @PersistenceContext(unitName = "APU_Medical-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FeedbackFacade() {
        super(Feedback.class);
    }
    
    public List<Feedback> findByAppointmentId(Long appointmentId) {
        return em.createQuery("SELECT f FROM Feedback f WHERE f.appointment.id = :appointmentId", Feedback.class)
                 .setParameter("appointmentId", appointmentId)
                 .getResultList();
    }
    
    public List<Appointment> findByCustomer(Customer customer) {
        return em.createQuery("SELECT a FROM Appointment a WHERE a.customer = :customer", Appointment.class)
                 .setParameter("customer", customer)
                 .getResultList();
    }
    
}
