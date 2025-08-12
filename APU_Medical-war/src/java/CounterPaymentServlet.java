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
//        if (session == null || session.getAttribute("loginUser") == null) {
//            response.sendRedirect("staffLogin.jsp");
//            return;
//        }
//        String role = (String) session.getAttribute("role");
//        if (!( "counter".equals(role) || "manager".equals(role) )) {
//            session.setAttribute("error", "Invalid User Role");
//            response.sendRedirect("stafflogin.jsp");
//            return;
//        }

        String action = request.getParameter("action");

        // --------- COLLECT PAYMENT (POST) ----------
        if ("POST".equalsIgnoreCase(request.getMethod()) && "collect".equalsIgnoreCase(action)) {
            try {
                String appointmentIdParam = request.getParameter("appointmentId");
                String amountParam = request.getParameter("amount");
                String method = request.getParameter("paymentMethod");

                if (appointmentIdParam == null || appointmentIdParam.isEmpty()) {
                    throw new IllegalArgumentException("Missing appointmentId.");
                }

                Long appointmentId = Long.valueOf(appointmentIdParam);
                Appointment appt = appointmentFacade.find(appointmentId);
                if (appt == null) throw new IllegalArgumentException("Appointment not found.");
                if (!"Completed".equalsIgnoreCase(appt.getStatus())) {
                    throw new IllegalStateException("Only completed appointments can be paid.");
                }

                double amount = Double.parseDouble(amountParam);

                Payment p = new Payment();
                p.setAmount(amount);
                p.setMethod(method);
                p.setStatus("Paid");
                p.setPaymentDate(java.time.LocalDate.now().toString());
                p.setPaymentTime(java.time.LocalTime.now().withNano(0).toString());
                p.setCustomer(appt.getCustomer());
                p.setAppointment(appt);
                p.setType(appt.getType());   // services
                // NOTE: We DO NOT call p.setDoctor(...). We’ll use appt.getDoctorName() when displaying.

                paymentFacade.create(p);

                // Mark appointment as settled
                appt.setOutstanding("0.00");
                appointmentFacade.edit(appt);

                session.setAttribute("success", "Payment recorded. Receipt RC" + p.getId());
            } catch (Exception ex) {
                session.setAttribute("error", "Failed to process payment: " + ex.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "CounterPaymentServlet");
            return;
        }

        // --------- BUILD TABLE DATA (GET) ----------
        List<Payment> allPayments = paymentFacade.findAll();

        // Today’s collection total
        String today = java.time.LocalDate.now().toString();
        double todaysCollection = 0.0;
        for (Payment p : allPayments) {
            if ("Paid".equalsIgnoreCase(p.getStatus()) && today.equals(p.getPaymentDate())) {
                todaysCollection += p.getAmount();
            }
        }

        // Payment History -> List<Map<String,String>>
        List<Map<String,String>> paymentHistory = new ArrayList<>();
        for (Payment p : allPayments) {
            if (!"Paid".equalsIgnoreCase(p.getStatus())) continue;

            Appointment appt = p.getAppointment();
            Map<String,String> row = new HashMap<>();
            row.put("receiptNo", "RC" + p.getId());
            row.put("patientName", p.getCustomer() != null ? p.getCustomer().getName() : "-");
            row.put("doctorName", appt != null ? appt.getDoctorName() : "-"); // use doctorName
            row.put("services", appt != null ? appt.getType() : p.getType());
            row.put("amount", String.format("%.2f", p.getAmount()));
            row.put("paymentMethod", p.getMethod());
            row.put("status", p.getStatus());
            paymentHistory.add(row);
        }

        // Pending Charges -> Completed appointments with no Paid payment
        List<Appointment> allAppts = appointmentFacade.findAll();
        List<Map<String,String>> pendingCharges = new ArrayList<>();
        double outstanding = 0.0;

        for (Appointment a : allAppts) {
            if (!"Completed".equalsIgnoreCase(a.getStatus())) continue;

            List<Payment> apptPays = paymentFacade.findByAppointmentId(a.getId());
            boolean hasPaid = apptPays.stream().anyMatch(pp -> "Paid".equalsIgnoreCase(pp.getStatus()));
            if (hasPaid) continue;

            String amtStr = a.getOutstanding() != null ? a.getOutstanding() : "0.00";
            double amt = 0.0;
            try { amt = Double.parseDouble(amtStr); } catch (Exception ignored) {}

            Map<String,String> row = new HashMap<>();
            row.put("appointmentId", String.valueOf(a.getId()));
            row.put("patientName", a.getCustomer() != null ? a.getCustomer().getName() : "-");
            row.put("doctorName", a.getDoctorName());              // use doctorName
            row.put("appointmentDate", a.getDate());
            row.put("services", a.getType());
            row.put("totalAmount", String.format("%.2f", amt));
            pendingCharges.add(row);
            outstanding += amt;
        }

        // Stats
        int receiptsIssued = paymentHistory.size();
        int pendingPayments = pendingCharges.size();

        // Send to JSP (maps)
        request.setAttribute("payments", paymentHistory);
        request.setAttribute("pendingCharges", pendingCharges);
        request.setAttribute("todaysCollection", todaysCollection);
        request.setAttribute("receiptsIssued", receiptsIssued);
        request.setAttribute("pendingPayments", pendingPayments);
        request.setAttribute("outstanding", outstanding);

        request.getRequestDispatcher("counterPayment.jsp").forward(request, response);
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
