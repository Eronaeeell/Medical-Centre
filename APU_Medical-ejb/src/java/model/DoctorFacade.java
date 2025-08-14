/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jee
 */
@Stateless
public class DoctorFacade extends AbstractFacade<Doctor> {

    @PersistenceContext(unitName = "APU_Medical-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DoctorFacade() {
        super(Doctor.class);
    }
    
    public Doctor findByEmail(String email) {
        try {
            return em.createQuery(
                    "SELECT d FROM Doctor d WHERE d.email = :email",
                    Doctor.class)
                .setParameter("email", email)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public long countActiveDoctors() {
        return em.createQuery(
            "SELECT COUNT(d) FROM Doctor d WHERE LOWER(d.status) = 'active'", Long.class)
            .getSingleResult();
    }
    
}
