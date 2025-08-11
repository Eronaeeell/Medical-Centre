/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import model.Payment;
import model.PaymentFacade;

/**
 *
 * @author Jee
 */
@WebServlet(urlPatterns = {"/customerPayment"})
public class customerPayment extends HttpServlet {

    @EJB
    private PaymentFacade paymentFacade;

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private CustomerFacade customerFacade;
    
    
    

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

        // Redirect to login if user not logged in
        if (customer == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        // ---------------- Handle Payment Submission ----------------
        if ("pay".equals(action) && "POST".equalsIgnoreCase(request.getMethod())) {
            try {
                double amount = Double.parseDouble(request.getParameter("amount"));
                String method = request.getParameter("method");
                String appointmentIdParam = request.getParameter("appointmentId");

                Appointment appointment = null;
                if (appointmentIdParam != null && !appointmentIdParam.isEmpty()) {
                    appointment = appointmentFacade.find(Long.parseLong(appointmentIdParam));

                    if (appointment == null || !"Completed".equalsIgnoreCase(appointment.getStatus())) {
                        session.setAttribute("error", "Only completed appointments can be paid.");
                        response.sendRedirect("customerPayment");
                        return;
                    }
                }

                // Create Payment entity
                Payment payment = new Payment();
                payment.setAmount(amount);
                payment.setMethod(method);
                payment.setStatus("Paid");
                payment.setPaymentDate(java.time.LocalDate.now().toString());
                payment.setPaymentTime(java.time.LocalTime.now().withNano(0).toString());
                payment.setCustomer(customer);
                payment.setAppointment(appointment);

                // Ensure type and doctor are set correctly
                if (appointment != null) {
                    payment.setType(appointment.getType()); // Service type
                }

                // Save into DB
                paymentFacade.create(payment);

                // Mark appointment as paid
                if (appointment != null) {
                    appointment.setOutstanding("0.00");
                    appointmentFacade.edit(appointment);
                }

                session.setAttribute("success", "Payment of RM " + amount + " successfully recorded.");
            } catch (Exception e) {
                session.setAttribute("error", "Failed to record payment: " + e.getMessage());
            }

            response.sendRedirect("customerPayment");
            return;
        }

        // ---------------- Build Payment Data ----------------
        List<Payment> payments = paymentFacade.findAll();

        // Calculate total collected amount for today
        String today = java.time.LocalDate.now().toString();
        double totalCollectedToday = 0;
        for (Payment p : payments) {
            if ("Paid".equalsIgnoreCase(p.getStatus()) && today.equals(p.getPaymentDate())) {
                totalCollectedToday += p.getAmount();
            }
        }

        // Pending Charges and Payment History
        List<Appointment> customerAppointments = appointmentFacade.findByCustomer(customer);
        List<Map<String, String>> pendingCharges = new ArrayList<>();
        List<Map<String, String>> paymentHistory = new ArrayList<>();

        for (Appointment a : customerAppointments) {
            List<Payment> appointmentPayments = paymentFacade.findByAppointmentId(a.getId());

            if (appointmentPayments.isEmpty() && "Completed".equalsIgnoreCase(a.getStatus())) {
                // Pending Charges set the valur to map
                Map<String, String> map = new HashMap<>();
                map.put("appointmentId", String.valueOf(a.getId()));
                map.put("patientName", customer.getName());
                map.put("doctorName", a.getDoctorName());
                map.put("date", a.getDate());
                map.put("services", a.getType());
                map.put("amount", a.getOutstanding());
                pendingCharges.add(map);
            } else {
                // Payment History
                for (Payment p : appointmentPayments) {
                    Appointment appt = p.getAppointment();
                    Map<String, String> map = new HashMap<>();
                    map.put("appointmentDate", appt != null ? appt.getDate() : "-");
                    map.put("patientName", p.getCustomer() != null ? p.getCustomer().getName() : "-");
                    map.put("doctorName", appt != null ? appt.getDoctorName() : "-");
                    map.put("services", appt != null ? appt.getType() : p.getType()); // fallback
                    map.put("amount", String.valueOf(p.getAmount()));
                    map.put("method", p.getMethod());
                    map.put("type", p.getStatus());
                    map.put("paymentDate", p.getPaymentDate());
                    map.put("paymentTime", p.getPaymentTime());
                    paymentHistory.add(map);
                }
            }
        }

        // Pass data to JSP
        request.setAttribute("payments", payments);
        request.setAttribute("pendingCharges", pendingCharges);
        request.setAttribute("paymentHistory", paymentHistory);
        request.setAttribute("totalCollectedToday", totalCollectedToday);

        request.getRequestDispatcher("customerPayment.jsp").forward(request, response);
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
