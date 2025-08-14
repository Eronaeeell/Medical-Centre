import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.AppointmentFacade;
import model.CounterStaffFacade;
import model.CustomerFacade;
import model.DoctorFacade;
import model.GeneratedReportFacade;
import model.Staff;

@WebServlet(urlPatterns = {"/ManagerDashboardServlet"})
public class ManagerDashboardServlet extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;
    
    @EJB
    private CounterStaffFacade counterStaffFacade;
    
    @EJB
    private CustomerFacade customerFacade;
    
    @EJB
    private DoctorFacade doctorFacade;
    
    @EJB
    private GeneratedReportFacade generatedReportFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Staff manager = (session != null) ? (Staff) session.getAttribute("manager") : null;

        if (manager == null) {
            response.sendRedirect("staff_login.jsp?error=Please+login+as+manager+first");
            return;
        }

        // Check if the staff member has manager role
        if (!"manager".equalsIgnoreCase(manager.getRole())) {
            response.sendRedirect("staff_login.jsp?error=Access+denied.+Manager+role+required");
            return;
        }

        try {
            // Fetch dashboard statistics using EJB facades
            
            // 1. Total Staff: Count from COUNTERSTAFF and DOCTOR tables (sum of both)
            long counterStaffCount = counterStaffFacade.count();
            long doctorCount = doctorFacade.count();
            long totalStaff = counterStaffCount + doctorCount;
            
            // 2. Today's Appointments: From APPOINTMENT table, filtered by today's date
            long todaysAppointments = appointmentFacade.countTodaysAppointments();
            
            // 3. Monthly Revenue: From APPOINTMENT table, sum of PAYMENT for current month
            Double monthlyRevenue = appointmentFacade.getMonthlyRevenue();
            
            // 4. Reports Generated: From GENERATEDREPORT table, count total records
            long reportsGenerated = generatedReportFacade.countTotalReports();
            
            // 5. Active Doctors: From DOCTOR table, count where STATUS = 'Active'
            long activeDoctors = doctorFacade.countActiveDoctors();
            
            // 6. Counter Staff: From COUNTERSTAFF table, count all records
            // (already fetched as counterStaffCount)
            
            // 7. Registered Customers: From CUSTOMER table, count all records
            long registeredCustomers = customerFacade.count();
            
            // 8. Pending Appointments: From APPOINTMENT table, count where STATUS = 'Pending'
            long pendingAppointments = appointmentFacade.countPendingAppointments();

            // Set attributes for JSP
            request.setAttribute("totalStaff", totalStaff);
            request.setAttribute("todaysAppointments", todaysAppointments);
            request.setAttribute("monthlyRevenue", String.format("RM %.2f", monthlyRevenue));
            request.setAttribute("reportsGenerated", reportsGenerated);
            request.setAttribute("activeDoctors", activeDoctors);
            request.setAttribute("counterStaffCount", counterStaffCount);
            request.setAttribute("registeredCustomers", registeredCustomers);
            request.setAttribute("pendingAppointments", pendingAppointments);
            request.setAttribute("manager", manager);

            // Forward to manager dashboard JSP
            request.getRequestDispatcher("managerDashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Log the error and redirect with error message
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            response.sendRedirect("staff_login.jsp?error=Dashboard+error+occurred");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Manager Dashboard Servlet using EJB facades";
    }// </editor-fold>

}