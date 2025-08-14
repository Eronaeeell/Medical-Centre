// ManagerAppointmentsServlet.java
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import model.AppointmentFacade;  // adjust package if different

@WebServlet("/ManagerAppointmentsServlet")
public class ManagerAppointmentsServlet extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("activePage", "appointments");

        // Pull rows from EJB (JPA under the hood)
        List<Map<String, Object>> appointments = appointmentFacade.fetchAllForManager();

        // Summary cards
        int totalAppointments = appointments.size();
        int completedAppointments = 0;
        int appointmentsWithFeedback = 0;

        for (Map<String, Object> a : appointments) {
            String status = String.valueOf(a.get("status"));
            String feedback = String.valueOf(a.get("feedback"));

            if ("completed".equalsIgnoreCase(status)) {
                completedAppointments++;
            }
            if (feedback != null && !feedback.trim().isEmpty() && !"null".equalsIgnoreCase(feedback)) {
                appointmentsWithFeedback++;
            }
        }

        request.setAttribute("appointments", appointments);
        request.setAttribute("totalAppointments", totalAppointments);
        request.setAttribute("completedAppointments", completedAppointments);
        request.setAttribute("appointmentsWithFeedback", appointmentsWithFeedback);

        request.getRequestDispatcher("managerAppointments.jsp").forward(request, response);
    }
}
