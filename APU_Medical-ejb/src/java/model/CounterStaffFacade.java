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
public class CounterStaffFacade extends AbstractFacade<CounterStaff> {

    @PersistenceContext(unitName = "APU_Medical-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CounterStaffFacade() {
        super(CounterStaff.class);
    }
    
    public CounterStaff findByEmail(String email) {
        try {
            return em.createQuery("SELECT cs FROM CounterStaff cs WHERE cs.email = :email", CounterStaff.class)
                         .setParameter("email", email)
                         .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
