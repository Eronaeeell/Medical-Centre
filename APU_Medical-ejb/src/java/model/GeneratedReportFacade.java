/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jee
 */
@Stateless
public class GeneratedReportFacade extends AbstractFacade<GeneratedReport> {

    @PersistenceContext(unitName = "APU_Medical-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GeneratedReportFacade() {
        super(GeneratedReport.class);
    }
    
    // Dashboard query method
    public long countTotalReports() {
        return em.createQuery(
            "SELECT COUNT(r) FROM GeneratedReport r", Long.class)
            .getSingleResult();
    }
    
}