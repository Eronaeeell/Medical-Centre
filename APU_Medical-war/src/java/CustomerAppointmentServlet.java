import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Appointment;
import model.AppointmentFacade;
import model.Customer;
import model.CustomerFacade;
import model.PaymentFacade;

@WebServlet(urlPatterns = {"/customerAppointmentsServlet"})
public class CustomerAppointmentServlet extends HttpServlet {

    @EJB
    private PaymentFacade paymentFacade;
    
    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private CustomerFacade customerFacade;
    
    private static int counter = 1; 
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user"); // ✅ use "user" from session

        if (customer == null) {
            session.setAttribute("errorMessage", "Session expired. Please log in again.");
            response.sendRedirect("login.jsp");
            return;
        }

        // Get all appointments of this customer
        List<Appointment> appointments = appointmentFacade.findByCustomer(customer);

        // Count completed and upcoming appointments
        long completedCount = appointments.stream()
            .filter(a -> "Completed".equalsIgnoreCase(a.getStatus()))
            .count();

        long upcomingCount = appointments.stream()
            .filter(a -> "Pending".equalsIgnoreCase(a.getStatus()) || "Accepted".equalsIgnoreCase(a.getStatus()))
            .count();
        
        double totalPaid = paymentFacade.sumPaidByCustomer(customer);

        // Set attributes for JSP
        request.setAttribute("appointments", appointments);
        request.setAttribute("completedCount", completedCount);
        request.setAttribute("upcomingCount", upcomingCount);
        request.setAttribute("totalPaid", totalPaid);

        // Forward to JSP
        request.getRequestDispatcher("customerAppointment.jsp").forward(request, response);
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
        return "Short description";
    }// </editor-fold>

}
