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
import model.CounterStaff;
import model.CustomerFacade;

@WebServlet(urlPatterns = {"/CounterAppointment"})
public class CounterAppointment extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private CustomerFacade customerFacade;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false); // don't create a new session

        CounterStaff counter = (CounterStaff) (session != null ? session.getAttribute("counter") : null);

        if (counter == null) {
            response.sendRedirect("staff_login.jsp?error=Please+login+first");
            return;
        }

        // ✅ Fetch all appointments from DB
        List<Appointment> allAppointments = appointmentFacade.findAll();

        // ✅ Prepare appointment data in Map for JSP
        List<Map<String, String>> appointmentList = new ArrayList<>();

        for (Appointment appt : allAppointments) {
            Map<String, String> data = new HashMap<>();
            data.put("id", String.valueOf(appt.getId()));
            data.put("patientName", appt.getCustomer() != null ? appt.getCustomer().getName() : "N/A");
            data.put("doctorName", appt.getDoctorName());
            data.put("date", appt.getDate());
            data.put("time", appt.getTime());
            data.put("type", appt.getType());
            data.put("status", appt.getStatus());
            data.put("notes", appt.getReason());
            appointmentList.add(data);
        }

        // ✅ Set appointments to request scope for JSP
        request.setAttribute("appointments", appointmentList);

        // ✅ Forward to JSP
        request.getRequestDispatcher("counterAppointment.jsp").forward(request, response);
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
