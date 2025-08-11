import java.io.IOException;
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
import java.time.LocalDate;
import java.util.stream.Collectors;
import model.FeedbackFacade;

@WebServlet(urlPatterns = {"/CustomerDashBoard"})
public class CustomerDashBoard extends HttpServlet {

    @EJB
    private FeedbackFacade feedbackFacade;

    @EJB
    private CustomerFacade customerFacade;

    @EJB
    private AppointmentFacade appointmentFacade;
    
    
    private static int counter = 1;

    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");

        if (customer == null) {
            session.setAttribute("errorMessage", "Session expired. Please log in again.");
            response.sendRedirect("login.jsp");
            return;
        }

        // ✅ Fetch all appointments for the logged-in customer
        List<Appointment> appointments = appointmentFacade.findByCustomer(customer);
        request.setAttribute("appointments", appointments);
        


        long completedCount = appointments.stream()
            .filter(a -> "Completed".equalsIgnoreCase(a.getStatus()))
            .count();

        long upcomingCount = appointments.stream()
            .filter(a -> "Pending".equalsIgnoreCase(a.getStatus()) || "Accepted".equalsIgnoreCase(a.getStatus()))
            .count();

        List<Appointment> upcomingAppointments = appointments.stream()
            .filter(a -> "Pending".equalsIgnoreCase(a.getStatus()))
            .collect(Collectors.toList());
        
        Appointment latestCompleted = appointments.stream()
            .filter(a -> "Completed".equalsIgnoreCase(a.getStatus()))
            .sorted((a1, a2) -> a2.getDate().compareTo(a1.getDate())) // descending by date
            .findFirst()
            .orElse(null);

        request.setAttribute("upcomingAppointments", upcomingAppointments);
        request.setAttribute("completedCount", completedCount);
        request.setAttribute("upcomingCount", upcomingCount);
        request.setAttribute("latestCompleted", latestCompleted);

        // ✅ Handle new appointment booking from form submission
        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getParameter("doctorName") != null) {
            String id = request.getParameter("id");
            String doctorName = request.getParameter("doctorName");
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            String type = request.getParameter("type");
            String reason = request.getParameter("reason");

            String outstanding = "0"; // Placeholder until staff updates
            String status = "Pending";
            String payment = "TBD by Staff";

            LocalDate selectedDate = LocalDate.parse(date);
            if (selectedDate.isBefore(LocalDate.now())) {
                session.setAttribute("errorMessage", "You cannot book an appointment for a past date.");
                response.sendRedirect("CustomerDashBoard");
                return;
            }

            //String doctorName, String date, String status, String type, String time, String reason, String payment, String feedback, String outstanding, Customer customer, List<Feedback> feedbacks)
            Appointment appointment = new Appointment(doctorName, date, status, type, time, reason, payment, outstanding, customer );
            appointmentFacade.create(appointment);

            response.sendRedirect("CustomerDashBoard"); 
            return;
        }

        // ✅ Forward to the JSP page with all data
        request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
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
        Customer customer = (Customer) session.getAttribute("user");

    if (customer == null) {
        session.setAttribute("errorMessage", "Session expired. Please log in again.");
        response.sendRedirect("login.jsp");
        return;
    }

    List<Appointment> appointments = appointmentFacade.findByCustomer(customer);

    long completedCount = appointments.stream()
        .filter(a -> "Completed".equalsIgnoreCase(a.getStatus()))
        .count();

    long upcomingCount = appointments.stream()
        .filter(a -> "Pending".equalsIgnoreCase(a.getStatus()) || "Accepted".equalsIgnoreCase(a.getStatus()))
        .count();

    List<Appointment> upcomingAppointments = appointments.stream()
        .filter(a -> "Pending".equalsIgnoreCase(a.getStatus()))
        .collect(Collectors.toList());

    request.setAttribute("upcomingAppointments", upcomingAppointments);
    request.setAttribute("completedCount", completedCount);
    request.setAttribute("upcomingCount", upcomingCount);

    request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
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
    HttpSession session = request.getSession();
    Customer customer = (Customer) session.getAttribute("user");

    if (customer == null) {
        session.setAttribute("errorMessage", "Session expired. Please log in again.");
        response.sendRedirect("login.jsp");
        return;
    }

    if (request.getParameter("doctorName") != null) {
        String doctorName = request.getParameter("doctorName");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String type = request.getParameter("type");
        String reason = request.getParameter("reason");

        LocalDate selectedDate = LocalDate.parse(date);
        if (selectedDate.isBefore(LocalDate.now())) {
            session.setAttribute("errorMessage", "You cannot book an appointment for a past date.");
            response.sendRedirect("CustomerDashBoard");
            return;
        }


        Appointment appointment = new Appointment(doctorName, date, "Pending", type, time, reason, "TBD by Staff", "0", customer);
        appointmentFacade.create(appointment);
    }

    response.sendRedirect("CustomerDashBoard");
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
