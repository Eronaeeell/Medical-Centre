import java.io.IOException;
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

@WebServlet(urlPatterns = {"/counterAddAppointment"})
public class counterAddAppointment extends HttpServlet {

    @EJB
    private CounterStaffFacade counterStaffFacade;

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private CustomerFacade customerFacade;
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        try {
            // ✅ Get Customer details from form
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phoneNo = request.getParameter("phoneNo");
            String age = request.getParameter("age");
            String gender = request.getParameter("gender");
            String ic = request.getParameter("ic");

            // ✅ Get Appointment details
            String doctorName = request.getParameter("doctorName");
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            String type = request.getParameter("type");
            String reason = request.getParameter("reason");

            //Create new Customer
            Customer customer = new Customer();
            customer.setName(name);
            customer.setEmail(email);
            customer.setPhoneNo(phoneNo);
            customer.setAge(age);
            customer.setGender(gender);
            customer.setIc(ic);
            customer.setPassword("default123"); // 🔹 or generate a password

            customerFacade.create(customer); // Auto-generates ID

            //Create new Appointment and link customer
            Appointment appointment = new Appointment();
            appointment.setDoctorName(doctorName);
            appointment.setDate(date);
            appointment.setTime(time);
            appointment.setType(type);
            appointment.setReason(reason);
            appointment.setStatus("Pending");
            appointment.setPayment("TBD by staff");
            appointment.setOutstanding("0");
            appointment.setCustomer(customer);

            appointmentFacade.create(appointment);

            // ✅ Success message & redirect to refresh the list
            session.setAttribute("successMessage", "Customer and Appointment created successfully!");
            response.sendRedirect("CounterAppointment"); // 🔹 redirect, not include()

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Something went wrong while adding appointment!");
            response.sendRedirect("CounterAppointment"); // 🔹 redirect to refresh even on error
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
        return "Short description";
    }// </editor-fold>

}
