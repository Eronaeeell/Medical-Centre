import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Appointment;
import model.AppointmentFacade;
import model.Customer;
import model.CustomerFacade;

@WebServlet(urlPatterns = {"/CounterUpdate"})
public class CounterUpdate extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private CustomerFacade customerFacade;
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("edit".equalsIgnoreCase(action)) {
                // ✏️ Appointment Update
                Long appointmentId = Long.parseLong(request.getParameter("id"));
                Appointment appointment = appointmentFacade.find(appointmentId);

                if (appointment != null) {
                    // Update appointment details
                    appointment.setDoctorName(request.getParameter("doctorName"));
                    appointment.setDate(request.getParameter("date"));
                    appointment.setTime(request.getParameter("time"));
                    appointment.setType(request.getParameter("type"));
                    appointment.setStatus(request.getParameter("status"));
                    appointment.setReason(request.getParameter("notes"));

                    appointmentFacade.edit(appointment);

                    // ✅ Forward directly to CounterAppointment servlet to reload data
                    request.setAttribute("success", "Appointment updated successfully");
                    request.getRequestDispatcher("CounterAppointment").forward(request, response);
                    return;
                } else {
                    request.setAttribute("error", "Appointment not found");
                    request.getRequestDispatcher("CounterAppointment").forward(request, response);
                    return;
                }

            } else {
                // ✏️ Customer Update
                Long id = Long.parseLong(request.getParameter("id"));
                Customer customer = customerFacade.find(id);

                if (customer != null) {
                    customer.setName(request.getParameter("name"));
                    customer.setEmail(request.getParameter("email"));
                    customer.setPhoneNo(request.getParameter("phoneNo"));
                    customer.setAge(request.getParameter("age"));
                    customer.setGender(request.getParameter("gender"));
                    customer.setIc(request.getParameter("ic"));

                    customerFacade.edit(customer);

                    request.setAttribute("success", "Customer updated successfully");
                    request.getRequestDispatcher("CounterAppointment").forward(request, response);
                    return;
                } else {
                    request.setAttribute("error", "Customer not found");
                    request.getRequestDispatcher("CounterAppointment").forward(request, response);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Unable to update record");
            request.getRequestDispatcher("CounterAppointment").forward(request, response);
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
