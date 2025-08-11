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
import model.CounterStaffFacade;
import model.CustomerFacade;
import model.Payment;
import model.PaymentFacade;

@WebServlet(urlPatterns = {"/CounterPaymentServlet"})
public class CounterPaymentServlet extends HttpServlet {

    @EJB
    private CounterStaffFacade counterStaffFacade;

    @EJB
    private AppointmentFacade appointmentFacade;
    

    @EJB
    private CustomerFacade customerFacade;

    @EJB
    private PaymentFacade paymentFacade;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        CounterStaff counter = (CounterStaff) (session != null ? session.getAttribute("counter") : null);

        if (counter == null) {
            response.sendRedirect("staff_login.jsp?error=Please+login+first");
            return;
        }

        String action = request.getParameter("action");

        // ✅ Payment Collection Logic
        if ("collect".equals(action) && "POST".equalsIgnoreCase(request.getMethod())) {
            try {
                double amount = Double.parseDouble(request.getParameter("amount"));
                String method = request.getParameter("paymentMethod");
                String patientName = request.getParameter("patientName");
                String doctorName = request.getParameter("doctorName");
                String services = request.getParameter("services");

                // Find matching appointment
                List<Appointment> allAppointments = appointmentFacade.findAll();
                Appointment appointment = null;
                for (Appointment a : allAppointments) {
                    if (a.getCustomer() != null && a.getCustomer().getName().equals(patientName)
                            && a.getDoctorName().equals(doctorName)
                            && a.getType().equals(services)) {
                        appointment = a;
                        break;
                    }
                }

                if (appointment == null) {
                    session.setAttribute("error", "Appointment not found!");
                    response.sendRedirect("CounterPayment");
                    return;
                }

                // Create Payment record
                Payment payment = new Payment();
                payment.setAmount(amount);
                payment.setMethod(method);
                payment.setStatus("Paid");
                payment.setType(appointment.getType());
                payment.setPaymentDate(java.time.LocalDate.now().toString());
                payment.setPaymentTime(java.time.LocalTime.now().withNano(0).toString());
                payment.setCustomer(appointment.getCustomer());
                payment.setAppointment(appointment);

                paymentFacade.create(payment);

                // Optional: mark appointment outstanding as 0
                appointment.setOutstanding("0.00");
                appointmentFacade.edit(appointment);

                session.setAttribute("success", "Payment of RM " + amount + " successfully recorded.");
            } catch (Exception e) {
                session.setAttribute("error", "Error processing payment: " + e.getMessage());
            }
            response.sendRedirect("CounterPayment");
            return;
        }

        // ✅ Build data for JSP
        List<Payment> payments = paymentFacade.findAll();
        List<Appointment> allAppointments = appointmentFacade.findAll();

        // Pending Charges
        List<Map<String, String>> pendingCharges = new ArrayList<>();
        for (Appointment a : allAppointments) {
            boolean paid = false;
            for (Payment p : payments) {
                if (p.getAppointment() != null && p.getAppointment().getId().equals(a.getId())) {
                    paid = true;
                    break;
                }
            }
            if ("Completed".equalsIgnoreCase(a.getStatus()) && !paid) {
                Map<String, String> map = new HashMap<>();
                map.put("patientName", a.getCustomer() != null ? a.getCustomer().getName() : "N/A");
                map.put("doctorName", a.getDoctorName());
                map.put("appointmentDate", a.getDate());
                map.put("services", a.getType());
                map.put("totalAmount", a.getOutstanding());
                pendingCharges.add(map);
            }
        }

        // Today's Collection
        String today = java.time.LocalDate.now().toString();
        double todaysCollection = 0.0;
        for (Payment p : payments) {
            if ("Paid".equalsIgnoreCase(p.getStatus()) && today.equals(p.getPaymentDate())) {
                todaysCollection += p.getAmount();
            }
        }

        // Set attributes for original JSP
        request.getServletContext().setAttribute("payments", buildPaymentMap(payments));
        request.getServletContext().setAttribute("pendingCharges", pendingCharges);

        request.getRequestDispatcher("counterPayment.jsp").forward(request, response);
    }

    // build payment map to match existing counterPayment in Payment Facade
    private List<Map<String, String>> buildPaymentMap(List<Payment> payments) {
        List<Map<String, String>> paymentList = new ArrayList<>();
        for (Payment p : payments) {
            Map<String, String> map = new HashMap<>();
            map.put("receiptNo", String.valueOf(p.getId()));
            map.put("patientName", p.getCustomer() != null ? p.getCustomer().getName() : "-");
            map.put("doctorName", p.getAppointment() != null ? p.getAppointment().getDoctorName() : "-");
            map.put("services", p.getType());
            map.put("amount", String.valueOf(p.getAmount()));
            map.put("paymentMethod", p.getMethod());
            map.put("status", p.getStatus());
            paymentList.add(map);
        }
        return paymentList;
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
