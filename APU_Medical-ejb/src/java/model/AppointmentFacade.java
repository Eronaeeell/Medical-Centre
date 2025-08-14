/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDate;
import java.time.YearMonth;
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
   
    public List<Appointment> findByCustomer(Customer customer) {
        return em.createQuery("SELECT a FROM Appointment a WHERE a.customer = :customer", Appointment.class)
                 .setParameter("customer", customer)
                 .getResultList();
    }
    
    public List<Map<String, Object>> fetchAllForManager() {
    @SuppressWarnings("unchecked")
    List<Object[]> rows = em.createNativeQuery(
        "SELECT CUSTOMER_ID, DOCTORNAME, \"DATE\", \"TIME\", STATUS, PAYMENT, FEEDBACK " +
        "FROM APPOINTMENT " +
        "ORDER BY \"DATE\" DESC, \"TIME\" DESC"
    ).getResultList();

    List<Map<String, Object>> out = new ArrayList<>();
    for (Object[] r : rows) {
        Map<String, Object> m = new HashMap<>();
        m.put("customerId",  r[0] != null ? r[0].toString() : "");
        m.put("doctorName",  r[1] != null ? r[1].toString() : "");
        m.put("date",        r[2] != null ? r[2].toString() : "");
        m.put("time",        r[3] != null ? r[3].toString() : "");
        m.put("status",      r[4] != null ? r[4].toString() : "");
        m.put("payment",     r[5] != null ? r[5].toString() : "");
        m.put("feedback",    r[6] != null ? r[6].toString() : "");
        out.add(m);
    }
    return out;
}
    
    // ---- NEW: get appointments by date range (date is stored as ISO string yyyy-MM-dd) ----
    public List<Appointment> findByDateRange(String from, String to) {
        return em.createQuery(
            "SELECT a FROM Appointment a WHERE a.date >= :from AND a.date <= :to",
            Appointment.class)
            .setParameter("from", from)
            .setParameter("to", to)
            .getResultList();
    }
    
    // NEW METHODS FOR DASHBOARD
    public long countTodaysAppointments() {
        String today = LocalDate.now().toString(); // Format: yyyy-MM-dd
        return em.createQuery(
            "SELECT COUNT(a) FROM Appointment a WHERE a.date = :today", Long.class)
            .setParameter("today", today)
            .getSingleResult();
    }
    
    public long countPendingAppointments() {
        return em.createQuery(
            "SELECT COUNT(a) FROM Appointment a WHERE LOWER(a.status) = 'pending'", Long.class)
            .getSingleResult();
    }
    
    public double calculateMonthlyRevenue() {
    YearMonth currentMonth = YearMonth.now();
    String firstDay = currentMonth.atDay(1).toString();    // yyyy-MM-dd
    String lastDay = currentMonth.atEndOfMonth().toString();

    List<Appointment> appointments = em.createQuery(
        "SELECT a FROM Appointment a WHERE a.date >= :firstDay AND a.date <= :lastDay AND a.payment IS NOT NULL AND a.payment <> ''",
        Appointment.class)
        .setParameter("firstDay", firstDay)
        .setParameter("lastDay", lastDay)
        .getResultList();

    double total = 0.0;
    for (Appointment a : appointments) {
        try {
            // Optionally, you can check for payment status (e.g., "Paid")
            total += Double.parseDouble(a.getPayment());
        } catch (NumberFormatException e) {
            // Optionally log the error for this appointment
        }
    }
    return total;
}

    // ========== REPORT GENERATION METHODS WITH DATE RANGE FILTERING ==========
    
    // Financial Report Methods
    public double getTotalRevenueByDateRange(String dateFrom, String dateTo) {
        List<Appointment> appointments = em.createQuery(
            "SELECT a FROM Appointment a WHERE a.date >= :dateFrom AND a.date <= :dateTo AND a.payment IS NOT NULL",
            Appointment.class)
            .setParameter("dateFrom", dateFrom)
            .setParameter("dateTo", dateTo)
            .getResultList();
        
        double total = 0.0;
        for (Appointment a : appointments) {
            try {
                if (a.getPayment() != null && !a.getPayment().trim().isEmpty()) {
                    total += Double.parseDouble(a.getPayment());
                }
            } catch (NumberFormatException e) {
                // Skip invalid payment amounts
            }
        }
        return total;
    }
    
    public long countCompletedAppointmentsByDateRange(String dateFrom, String dateTo) {
        return em.createQuery(
            "SELECT COUNT(a) FROM Appointment a WHERE a.date >= :dateFrom AND a.date <= :dateTo AND LOWER(a.status) = 'completed'",
            Long.class)
            .setParameter("dateFrom", dateFrom)
            .setParameter("dateTo", dateTo)
            .getSingleResult();
    }
    
    public long countPendingPaymentsByDateRange(String dateFrom, String dateTo) {
        return em.createQuery(
            "SELECT COUNT(a) FROM Appointment a WHERE a.date >= :dateFrom AND a.date <= :dateTo AND LOWER(a.status) = 'pending'",
            Long.class)
            .setParameter("dateFrom", dateFrom)
            .setParameter("dateTo", dateTo)
            .getSingleResult();
    }
    
    public double getAveragePaymentByDateRange(String dateFrom, String dateTo) {
        List<Appointment> appointments = em.createQuery(
            "SELECT a FROM Appointment a WHERE a.date >= :dateFrom AND a.date <= :dateTo AND a.payment IS NOT NULL",
            Appointment.class)
            .setParameter("dateFrom", dateFrom)
            .setParameter("dateTo", dateTo)
            .getResultList();
        
        double total = 0.0;
        int count = 0;
        for (Appointment a : appointments) {
            try {
                if (a.getPayment() != null && !a.getPayment().trim().isEmpty()) {
                    total += Double.parseDouble(a.getPayment());
                    count++;
                }
            } catch (NumberFormatException e) {
                // Skip invalid payment amounts
            }
        }
        return count > 0 ? total / count : 0.0;
    }
    
    // FIXED Staff Performance Report Methods
    public String getTopDoctorByDateRange(String dateFrom, String dateTo) {
        try {
            // Use native SQL to ensure column name compatibility
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(
                "SELECT DOCTORNAME, COUNT(*) as appointment_count " +
                "FROM APPOINTMENT " +
                "WHERE \"DATE\" >= ? AND \"DATE\" <= ? " +
                "AND DOCTORNAME IS NOT NULL AND DOCTORNAME <> '' " +
                "GROUP BY DOCTORNAME " +
                "ORDER BY COUNT(*) DESC"
            )
            .setParameter(1, dateFrom)
            .setParameter(2, dateTo)
            .setMaxResults(1)
            .getResultList();
            
            if (!results.isEmpty()) {
                Object[] result = results.get(0);
                String doctorName = result[0] != null ? result[0].toString() : "Unknown";
                Number appointmentCount = (Number) result[1];
                return doctorName + " (" + appointmentCount + " appointments)";
            }
            return "No appointments found in date range";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving top doctor data: " + e.getMessage();
        }
    }
    
    public String getTopFeedbackScore(String dateFrom, String dateTo) {
        try {
            // Use native SQL to get the doctor with highest feedback score
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(
                "SELECT DOCTORNAME, " +
                "AVG(CASE " +
                "WHEN UPPER(FEEDBACK) LIKE '%EXCELLENT%' OR FEEDBACK LIKE '%5%' THEN 5 " +
                "WHEN UPPER(FEEDBACK) LIKE '%VERY GOOD%' OR UPPER(FEEDBACK) LIKE '%GOOD%' OR FEEDBACK LIKE '%4%' THEN 4 " +
                "WHEN UPPER(FEEDBACK) LIKE '%AVERAGE%' OR UPPER(FEEDBACK) LIKE '%OKAY%' OR FEEDBACK LIKE '%3%' THEN 3 " +
                "WHEN UPPER(FEEDBACK) LIKE '%POOR%' OR UPPER(FEEDBACK) LIKE '%BAD%' OR FEEDBACK LIKE '%2%' THEN 2 " +
                "WHEN UPPER(FEEDBACK) LIKE '%VERY POOR%' OR UPPER(FEEDBACK) LIKE '%TERRIBLE%' OR FEEDBACK LIKE '%1%' THEN 1 " +
                "ELSE 3 END) as avg_score, " +
                "COUNT(*) as feedback_count " +
                "FROM APPOINTMENT " +
                "WHERE \"DATE\" >= ? AND \"DATE\" <= ? " +
                "AND DOCTORNAME IS NOT NULL AND DOCTORNAME <> '' " +
                "AND FEEDBACK IS NOT NULL AND FEEDBACK <> '' " +
                "GROUP BY DOCTORNAME " +
                "ORDER BY AVG(CASE " +
                "WHEN UPPER(FEEDBACK) LIKE '%EXCELLENT%' OR FEEDBACK LIKE '%5%' THEN 5 " +
                "WHEN UPPER(FEEDBACK) LIKE '%VERY GOOD%' OR UPPER(FEEDBACK) LIKE '%GOOD%' OR FEEDBACK LIKE '%4%' THEN 4 " +
                "WHEN UPPER(FEEDBACK) LIKE '%AVERAGE%' OR UPPER(FEEDBACK) LIKE '%OKAY%' OR FEEDBACK LIKE '%3%' THEN 3 " +
                "WHEN UPPER(FEEDBACK) LIKE '%POOR%' OR UPPER(FEEDBACK) LIKE '%BAD%' OR FEEDBACK LIKE '%2%' THEN 2 " +
                "WHEN UPPER(FEEDBACK) LIKE '%VERY POOR%' OR UPPER(FEEDBACK) LIKE '%TERRIBLE%' OR FEEDBACK LIKE '%1%' THEN 1 " +
                "ELSE 3 END) DESC"
            )
            .setParameter(1, dateFrom)
            .setParameter(2, dateTo)
            .setMaxResults(1)
            .getResultList();
            
            if (!results.isEmpty()) {
                Object[] result = results.get(0);
                String doctorName = result[0] != null ? result[0].toString() : "Unknown";
                Number avgScore = (Number) result[1];
                Number feedbackCount = (Number) result[2];
                
                return String.format("%.1f/5.0 - %s (%s reviews)", 
                    avgScore.doubleValue(), doctorName, feedbackCount.toString());
            }
            
            return "No feedback data found in date range";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving feedback data: " + e.getMessage();
        }
    }
    
    // Appointment Analytics Report Methods
    public long countTotalAppointmentsByDateRange(String dateFrom, String dateTo) {
        return em.createQuery(
            "SELECT COUNT(a) FROM Appointment a WHERE a.date >= :dateFrom AND a.date <= :dateTo",
            Long.class)
            .setParameter("dateFrom", dateFrom)
            .setParameter("dateTo", dateTo)
            .getSingleResult();
    }
    
    public long countScheduledAppointmentsByDateRange(String dateFrom, String dateTo) {
        return em.createQuery(
            "SELECT COUNT(a) FROM Appointment a WHERE a.date >= :dateFrom AND a.date <= :dateTo AND LOWER(a.status) = 'scheduled'",
            Long.class)
            .setParameter("dateFrom", dateFrom)
            .setParameter("dateTo", dateTo)
            .getSingleResult();
    }
    
    public String getMostCommonTypeByDateRange(String dateFrom, String dateTo) {
        @SuppressWarnings("unchecked")
        List<Object[]> results = em.createQuery(
            "SELECT a.type, COUNT(a) FROM Appointment a WHERE a.date >= :dateFrom AND a.date <= :dateTo " +
            "GROUP BY a.type ORDER BY COUNT(a) DESC")
            .setParameter("dateFrom", dateFrom)
            .setParameter("dateTo", dateTo)
            .setMaxResults(1)
            .getResultList();
        
        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            return result[0] + " (" + result[1] + ")";
        }
        return "No appointment types found";
    }
    
    public String getFeedbackSummaryByDateRange(String dateFrom, String dateTo) {
        @SuppressWarnings("unchecked")
        List<Object[]> results = em.createQuery(
            "SELECT a.feedback, COUNT(a) FROM Appointment a WHERE a.date >= :dateFrom AND a.date <= :dateTo " +
            "AND a.feedback IS NOT NULL AND a.feedback <> '' GROUP BY a.feedback")
            .setParameter("dateFrom", dateFrom)
            .setParameter("dateTo", dateTo)
            .getResultList();
        
        int excellent = 0, good = 0, average = 0, poor = 0, total = 0;
        for (Object[] result : results) {
            String feedback = (String) result[0];
            int count = ((Number) result[1]).intValue();
            total += count;
            
            if (feedback != null) {
                String feedbackLower = feedback.toLowerCase();
                if (feedbackLower.contains("excellent") || feedbackLower.contains("5")) {
                    excellent += count;
                } else if (feedbackLower.contains("good") || feedbackLower.contains("4")) {
                    good += count;
                } else if (feedbackLower.contains("average") || feedbackLower.contains("okay") || feedbackLower.contains("3")) {
                    average += count;
                } else if (feedbackLower.contains("poor") || feedbackLower.contains("bad") || 
                          feedbackLower.contains("2") || feedbackLower.contains("1")) {
                    poor += count;
                } else {
                    average += count; // Default unknown feedback to average
                }
            }
        }
        
        if (total == 0) {
            return "No feedback found in date range";
        }
        
        return String.format("Excellent (%d), Good (%d), Average (%d), Poor (%d) - Total: %d", 
                           excellent, good, average, poor, total);
    }
}