//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.ejb.EJB;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import model.Appointment;
//import model.AppointmentFacade;
//import model.Customer;
//import model.CustomerFacade;
//import model.Doctor;
//
//@WebServlet(urlPatterns = {"/DoctorAppointment"})
//public class DoctorAppointment extends HttpServlet {
//
//    @EJB
//    private CustomerFacade customerFacade;
//
//    @EJB
//    private AppointmentFacade appointmentFacade;
//    
//    private static final DateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");
//    private static final DateFormat TIME_FMT = new SimpleDateFormat("HH:mm");
//    
//
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        // 1) Auth guard
//        HttpSession session = request.getSession(false);
//        Doctor doctor = (session != null) ? (Doctor) session.getAttribute("doctor") : null;
//        if (doctor == null) {
//            response.sendRedirect("staff_login.jsp?error=Please sign in as a doctor.");
//            return;
//        }
//
//        // 2) Load this doctor's appointments
//        List<Appointment> appts = appointmentFacade.findByDoctor(doctor); // NamedQuery method per your pattern
//        if (appts == null) appts = Collections.emptyList();
//
//        // 3) Transform to maps for JSP
//        List<Map<String, Object>> rows = new ArrayList<>();
//        for (Appointment a : appts) {
//            Map<String, Object> m = new HashMap<>();
//
//            // Adapt these getters to your exact entity fields
//            Date apptDate = a.getDate();    // e.g., java.util.Date
//            Date apptTime = a.getAppointmentTime();    // or stored as Date/LocalTime/String
//            String status  = safe(a.getStatus());      // "Scheduled", "Confirmed", "In Progress", "Completed", "Cancelled"
//            String reason  = safe(a.getReason());
//            String type    = safe(a.getAppointmentType());
//
//            Customer c = a.getCustomer();
//            String patientName = (c != null) ? safe(c.getName()) : "-";
//            String patientGender = (c != null) ? safe(c.getGender()) : "-";
//            Integer patientAge = calcAge(c); // from DOB if available; null-safe
//
//            // Format
//            m.put("appointmentDate", apptDate != null ? DATE_FMT.format(apptDate) : "-");
//            m.put("appointmentTime", apptTime != null ? TIME_FMT.format(apptTime) : "-");
//            m.put("status", status);
//            m.put("reason", reason);
//            m.put("appointmentType", type);
//            m.put("patientName", patientName);
//            m.put("patientGender", patientGender);
//            m.put("patientAge", patientAge != null ? patientAge : "-");
//
//            rows.add(m);
//        }
//
//        // 4) Counters for summary cards
//        LocalDate today = LocalDate.now();
//        int todayCount = 0, completedToday = 0, inProgressToday = 0, upcomingCount = 0;
//
//        for (Appointment a : appts) {
//            Date apptDate = a.getAppointmentDate();
//            LocalDate d = toLocalDate(apptDate);
//            String status = safe(a.getStatus());
//
//            if (d != null && d.equals(today)) {
//                todayCount++;
//                if ("Completed".equalsIgnoreCase(status)) completedToday++;
//                if ("In Progress".equalsIgnoreCase(status)) inProgressToday++;
//            }
//            if (d != null && d.isAfter(today)) {
//                upcomingCount++;
//            }
//        }
//
//        // 5) Set attributes for the JSP
//        request.setAttribute("allAppointments", rows);
//        request.setAttribute("todayCount", todayCount);
//        request.setAttribute("completedTodayCount", completedToday);
//        request.setAttribute("inProgressTodayCount", inProgressToday);
//        request.setAttribute("upcomingCount", upcomingCount);
//
//        // 6) Forward to JSP (use your actual JSP filename here)
//        request.getRequestDispatcher("doctorAppointment.jsp").forward(request, response);
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
