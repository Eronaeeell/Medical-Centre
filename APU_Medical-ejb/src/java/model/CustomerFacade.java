/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jee
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "APU_Medical-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerFacade() {
        super(Customer.class);
    }
    
    public Customer findByUsername(String name) {
        try {
            return em.createQuery("SELECT c FROM Customer c WHERE c.name = :name", Customer.class)
                     .setParameter("name", name)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean validateLogin(String name, String password) {
        Customer customer = findByUsername(name);
        return customer != null && customer.getPassword().equals(password);
    }

    public Customer findByName(String name) {
        List<Customer> list = em.createQuery("SELECT c FROM Customer c WHERE c.name = :name", Customer.class)
                                .setParameter("name", name)
                                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<Customer> searchByKeyword(String keyword) {
    return em.createQuery("SELECT c FROM Customer c WHERE " +
            "LOWER(c.name) LIKE :kw OR LOWER(c.ic) LIKE :kw OR LOWER(c.email) LIKE :kw OR LOWER(c.phoneNo) LIKE :kw", Customer.class)
            .setParameter("kw", "%" + keyword.toLowerCase() + "%")
            .getResultList();
    }
    
    public long countAllCustomers() {
        return em.createQuery("SELECT COUNT(c) FROM Customer c", Long.class)
            .getSingleResult();
    }
    
    
}
