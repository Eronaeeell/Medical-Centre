import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.Appointment;
import model.AppointmentFacade;
import model.Customer;
import model.CustomerFacade;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private CustomerFacade customerFacade;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        Customer customer = customerFacade.findByName(name);

        if (customer != null && customer.getPassword().equals(password)) {

            HttpSession session = request.getSession();
            session.setAttribute("user", customer); // Store full Customer object

            // ✅ Extract values immediately after setting
            Long id = customer.getId();
            String fullName = customer.getName();
            System.out.println("Customer ID: " + id);
            System.out.println("Customer Name: " + fullName);

            List<Appointment> appointments = appointmentFacade.findByCustomer(customer);

            // Proceed to calculate counts...
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

            // Forward to dashboard
            request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);

        } else {
            request.setAttribute("Error", "Invalid Name and Password!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }
}
