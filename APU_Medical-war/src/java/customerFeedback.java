import java.io.IOException;
import java.util.ArrayList;
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
import model.Customer;
import model.CustomerFacade;
import model.Doctor;
import model.DoctorFacade;
import model.Feedback;
import model.FeedbackFacade;
//import model.StaffFacade;

@WebServlet(urlPatterns = {"/customerFeedback"})
public class customerFeedback extends HttpServlet {

    @EJB
    private DoctorFacade doctorFacade;

    //@EJB
    //private StaffFacade staffFacade;

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private FeedbackFacade feedbackFacade;

    @EJB
    private CustomerFacade customerFacade;
    
    
    
    
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");

        if (customer == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // ✅ Handle form submission (POST)
        String action = request.getParameter("action");
        if ("submitFeedback".equals(action)) {
            try {
                
            Long doctorId = Long.parseLong(request.getParameter("id"));
            Doctor doctor = doctorFacade.find(doctorId);
                
                // Get parameters from form
                Long appointmentId = Long.parseLong(request.getParameter("id"));
                String rateDate = java.time.LocalDate.now().toString();
                int doctorRating = Integer.parseInt(request.getParameter("doctorRating"));
                int counterStaffRating = Integer.parseInt(request.getParameter("counterStaffRating"));
                String overallexperience = request.getParameter("overallexperience");
                String doctorFeedback = request.getParameter("doctorFeedback");
                String staffFeedback = request.getParameter("staffFeedback");

                // Find appointment
                Appointment appointment = appointmentFacade.find(appointmentId);
                if (appointment == null) {
                    System.out.println("❌ Appointment not found for ID: " + appointmentId);
                    session.setAttribute("error", "Invalid appointment.");
                    response.sendRedirect("customerFeedback");
                    return;
                }

                // Create Feedback record
                Feedback feedback = new Feedback(rateDate,doctorRating,overallexperience,appointment,doctor,counterStaffRating,staffFeedback,doctorFeedback);
                // Persist feedback
                feedbackFacade.create(feedback);


                session.setAttribute("success", "Feedback submitted successfully!");
            } catch (Exception e) {
                session.setAttribute("error", "Failed to submit feedback: " + e.getMessage());
            }

            // Redirect back to refresh the list
            response.sendRedirect("customerFeedback");
            return;
        }

        // ✅ Handle GET - Load pending feedback and history
        List<Map<String, String>> pendingFeedback = new ArrayList<>();
        List<Map<String, String>> feedbackHistory = new ArrayList<>();

        // Fetch all appointments for customer
        List<Appointment> appointments = appointmentFacade.findByCustomer(customer);

        for (Appointment a : appointments) {
            List<Feedback> feedbacks = feedbackFacade.findByAppointmentId(a.getId());

            if (feedbacks.isEmpty() && "Completed".equalsIgnoreCase(a.getStatus())) {
                // Pending feedback
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(a.getId()));
                map.put("date", a.getDate());
                map.put("doctorName", a.getDoctorName());
                map.put("counterStaff", "Random"); // Update with real data if available
                map.put("type", a.getType());
                pendingFeedback.add(map);
            } else {
                // Feedback already exists
                for (Feedback f : feedbacks) {
                    Map<String, String> map = new HashMap<>();
                    map.put("appointmentDate", a.getDate());
                    map.put("doctorName", a.getDoctorName());
                    map.put("counterStaff", "N/A");
                    map.put("doctorRating", String.valueOf(f.getDoctorRating()));
                    map.put("overallExperience", f.getOverallexperience());
                    map.put("status", "Completed");
                    feedbackHistory.add(map);
                }
            }
        }

        // Pass data to JSP
        request.setAttribute("pendingFeedback", pendingFeedback);
        request.setAttribute("feedbackHistory", feedbackHistory);

        // Debugging
        System.out.println("pendingFeedback size: " + pendingFeedback.size());
        System.out.println("feedbackHistory size: " + feedbackHistory.size());
        
        request.getRequestDispatcher("customerFeedback.jsp").forward(request, response);
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
