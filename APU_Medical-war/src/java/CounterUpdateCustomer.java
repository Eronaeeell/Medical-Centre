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
import model.CounterStaffFacade;
import model.Customer;
import model.CustomerFacade;

@WebServlet(urlPatterns = {"/CounterUpdateCustomer"})
public class CounterUpdateCustomer extends HttpServlet {

    @EJB
    private CustomerFacade customerFacade;

    @EJB
    private CounterStaffFacade counterStaffFacade;

    @EJB
    private AppointmentFacade appointmentFacade;
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String action = request.getParameter("action"); // 🔹 differentiate actions

        try {
            if ("edit".equalsIgnoreCase(action)) {
                // ✅ Update existing customer
                Long customerId = Long.parseLong(request.getParameter("id"));
                Customer customer = customerFacade.find(customerId);

                if (customer != null) {
                    customer.setName(request.getParameter("name"));
                    customer.setEmail(request.getParameter("email"));
                    customer.setPhoneNo(request.getParameter("phone"));
                    customer.setAge(request.getParameter("age"));
                    customer.setGender(request.getParameter("gender"));
                    customer.setIc(request.getParameter("ic"));

                    customerFacade.edit(customer); // 🔹 update database
                    session.setAttribute("successMessage", "Customer details updated successfully!");
                } else {
                    session.setAttribute("errorMessage", "Customer not found for update!");
                }

                response.sendRedirect("CounterAppointment"); // refresh list
                return;
            }

            // ✅ Handle new appointment & customer creation
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phoneNo = request.getParameter("phoneNo");
            String age = request.getParameter("age");
            String gender = request.getParameter("gender");
            String ic = request.getParameter("ic");

            // Appointment details
            String doctorName = request.getParameter("doctorName");
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            String type = request.getParameter("type");
            String reason = request.getParameter("reason");

            // Create customer
            Customer customer = new Customer();
            customer.setName(name);
            customer.setEmail(email);
            customer.setPhoneNo(phoneNo);
            customer.setAge(age);
            customer.setGender(gender);
            customer.setIc(ic);
            customer.setPassword("default123");

            customerFacade.create(customer);

            // Create appointment
            Appointment appointment = new Appointment(doctorName,date,time,type,reason,"Pending","TBD by staff","0",customer);
//            appointment.setDoctorName(doctorName);
//            appointment.setDate(date);
//            appointment.setTime(time);
//            appointment.setType(type);
//            appointment.setReason(reason);
//            appointment.setStatus("Pending");
//            appointment.setPayment("TBD by staff");
//            appointment.setOutstanding("0");
//            appointment.setCustomer(customer);

            appointmentFacade.create(appointment);

            session.setAttribute("successMessage", "Customer and Appointment created successfully!");
            response.sendRedirect("CounterAppointment");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error while processing request: " + e.getMessage());
            response.sendRedirect("CounterAppointment");
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
