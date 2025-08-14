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
public class StaffFacade extends AbstractFacade<Staff> {

    @PersistenceContext(unitName = "APU_Medical-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StaffFacade() {
        super(Staff.class);
    }
    
    public Staff findByEmailAndRole(String email, String role) {
        try {
            return em.createQuery(
                    "SELECT s FROM Staff s WHERE s.email = :email AND s.role = :role",
                    Staff.class)
                .setParameter("email", email)
                .setParameter("role", role)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
