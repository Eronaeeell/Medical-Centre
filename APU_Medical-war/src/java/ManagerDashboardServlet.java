package medicalcentre;

import model.AppointmentFacade;
import model.CounterStaffFacade;
import model.CustomerFacade;
import model.DoctorFacade;
import model.GeneratedReportFacade;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/ManagerDashboardServlet")
public class ManagerDashboardServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ManagerDashboardServlet.class.getName());

    @EJB
    private DoctorFacade doctorFacade;
    
    @EJB
    private CounterStaffFacade counterStaffFacade;
    
    @EJB
    private CustomerFacade customerFacade;
    
    @EJB
    private AppointmentFacade appointmentFacade;
    
    @EJB
    private GeneratedReportFacade generatedReportFacade;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Map<String, Object> stats = new HashMap<>();

        try {
            // 1. Total Staff (CounterStaff + Doctors)
            long counterStaffCount = counterStaffFacade.countAllCounterStaff();
            long doctorCount = doctorFacade.count();
            stats.put("totalStaff", counterStaffCount + doctorCount);

            // 2. Active Doctors
            stats.put("activeDoctors", doctorFacade.countActiveDoctors());

            // 3. Counter Staff
            stats.put("counterStaff", counterStaffCount);

            // 4. Today's Appointments
            stats.put("todaysAppointments", appointmentFacade.countTodaysAppointments());

            // 5. Monthly Revenue
            stats.put("monthlyRevenue", appointmentFacade.calculateMonthlyRevenue());

            // 6. Registered Customers
            stats.put("registeredCustomers", customerFacade.countAllCustomers());

            // 7. Pending Appointments
            stats.put("pendingAppointments", appointmentFacade.countPendingAppointments());

            // 8. Reports Generated
            stats.put("reportsGenerated", generatedReportFacade.countAllReports());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching dashboard statistics", e);
            throw new ServletException("Error fetching dashboard data", e);
        }

        // Set attributes for JSP
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }

        request.getRequestDispatcher("managerDashboard.jsp").forward(request, response);
    }
}