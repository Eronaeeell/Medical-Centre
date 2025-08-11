import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Appointment;
import model.AppointmentFacade;
import model.CounterStaff;
import model.CounterStaffFacade;
import model.Customer;
import model.CustomerFacade;

@WebServlet(urlPatterns = {"/CounterCustomerServlet"})
public class CounterCustomerServlet extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private CounterStaffFacade counterStaffFacade;

    @EJB
    private CustomerFacade customerFacade;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(); // Don't create new session if none exists

        CounterStaff counter = (CounterStaff) (session != null ? session.getAttribute("counter") : null);

        if (counter == null) {
            // Not logged in, redirect to staff login
            response.sendRedirect("staff_login.jsp?error=Please+login+first");
            return;
        }

        // Logged in, fetch customers
        List<Customer> customers = customerFacade.findAll();
        request.setAttribute("customers", customers);

        // Forward to JSP to render customers
        request.getRequestDispatcher("counterCustomer.jsp").forward(request, response);
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

        HttpSession session = request.getSession();
        CounterStaff counter = (CounterStaff) session.getAttribute("counter");

        if (counter == null) {
            response.sendRedirect("staff_login.jsp?error=Please+login+first");
            return;
        }

        // Fetch all customers
        List<Customer> customers = customerFacade.findAll();

        // Build a map of CustomerId -> latest appointment status
        Map<Long, String> latestStatusMap = new HashMap<>();

        for (Customer c : customers) {
            // Fetch appointments for this customer
            List<Appointment> appointments = appointmentFacade.findByCustomer(c);

            // If appointments exist, get the latest one (you can sort or pick last)
            if (!appointments.isEmpty()) {
                // Assuming list is sorted, else sort by date
                Appointment latest = appointments.get(appointments.size() - 1);
                latestStatusMap.put(c.getId(), latest.getStatus());
            } else {
                latestStatusMap.put(c.getId(), "No Appointments");
            }
        }

        // Debug logs
        System.out.println("Customers count: " + customers.size());
        System.out.println("Status Map: " + latestStatusMap);

        request.setAttribute("customers", customers);
        request.setAttribute("latestStatusMap", latestStatusMap);

        // Forward to JSP
        request.getRequestDispatcher("counterCustomer.jsp").forward(request, response);
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
