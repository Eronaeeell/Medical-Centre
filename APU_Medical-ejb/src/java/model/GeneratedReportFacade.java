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
 * @author Eronaeeell
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
    
    // NEW METHOD FOR DASHBOARD
    public long countAllReports() {
        return em.createQuery("SELECT COUNT(gr) FROM GeneratedReport gr", Long.class)
            .getSingleResult();
    }
    
    // Get all reports ordered by ID descending (most recent first)
    public List<GeneratedReport> findAllOrderedByIdDesc() {
        return em.createQuery("SELECT gr FROM GeneratedReport gr ORDER BY gr.id DESC", GeneratedReport.class)
                .getResultList();
    }
    
    // Save a new generated report
    public void saveReport(String name, String date, String type, String status, String size,
                          String dateFrom, String dateTo, String summary1, String summary2, 
                          String summary3, String summary4, String summary5) {
        GeneratedReport report = new GeneratedReport();
        report.setName(name);
        report.setDate(date);
        report.setType(type);
        report.setStatus(status);
        report.setSize(size);
        report.setDateFrom(dateFrom);  
        report.setDateTo(dateTo);      
        report.setSummary1(summary1);
        report.setSummary2(summary2);
        report.setSummary3(summary3);
        report.setSummary4(summary4);
        report.setSummary5(summary5);
        
        create(report);
    }
}