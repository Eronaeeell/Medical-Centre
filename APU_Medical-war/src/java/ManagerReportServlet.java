import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import model.*;

@WebServlet("/ManagerReportServlet")
public class ManagerReportServlet extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;
    
    @EJB
    private DoctorFacade doctorFacade;
    
    @EJB
    private CounterStaffFacade counterStaffFacade;
    
    @EJB
    private GeneratedReportFacade generatedReportFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, Object>> recentReports = convertReportsToMaps(generatedReportFacade.findAllOrderedByIdDesc());
        request.setAttribute("recentReports", recentReports);
        request.setAttribute("activePage", "report");
        request.getRequestDispatcher("managerReport.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type");
        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");

        String typeName = getReportTypeName(type);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        String reportName = typeName + " (" + dateFrom + " to " + dateTo + ")";
        double sizeMB = Math.round((new Random().nextDouble() * 3 + 1) * 10.0) / 10.0;

        // Prepare summary fields for DB
        String summary1 = null, summary2 = null, summary3 = null, summary4 = null, summary5 = null;

        if ("financial".equals(type)) {
            Map<String, Object> financialSummary = getFinancialReportSummary(dateFrom, dateTo);
            request.setAttribute("financialSummary", financialSummary);
            summary1 = String.format("%.2f", financialSummary.get("totalRevenue"));
            summary2 = String.valueOf(financialSummary.get("completedAppointments"));
            summary3 = String.valueOf(financialSummary.get("pendingPayments"));
            summary4 = String.format("%.2f", financialSummary.get("avgPayment"));
        } else if ("staff".equals(type)) {
            Map<String, Object> staffSummary = getStaffPerformanceSummary(dateFrom, dateTo);
            request.setAttribute("staffSummary", staffSummary);
            summary1 = String.valueOf(staffSummary.get("activeDoctors"));
            summary2 = String.valueOf(staffSummary.get("counterStaff"));
            summary3 = String.valueOf(staffSummary.get("topDoctor"));
            summary4 = String.valueOf(staffSummary.get("topDoctorFeedback"));
        } else if ("appointments".equals(type)) {
            Map<String, Object> apptSummary = getAppointmentAnalyticsSummary(dateFrom, dateTo);
            request.setAttribute("apptSummary", apptSummary);
            summary1 = String.valueOf(apptSummary.get("totalAppointments"));
            summary2 = String.valueOf(apptSummary.get("completed"));
            summary3 = String.valueOf(apptSummary.get("scheduled"));
            summary4 = String.valueOf(apptSummary.get("mostCommonType"));
            summary5 = String.valueOf(apptSummary.get("feedbackSummary"));
        }

        // Save report using EJB
        generatedReportFacade.saveReport(reportName, today, typeName, "Generated", 
                String.format("%.1f MB", sizeMB), dateFrom, dateTo, 
                summary1, summary2, summary3, summary4, summary5);

        List<Map<String, Object>> recentReports = convertReportsToMaps(generatedReportFacade.findAllOrderedByIdDesc());

        request.setAttribute("recentReports", recentReports);
        request.setAttribute("activePage", "report");
        request.setAttribute("success", "Report \"" + reportName + "\" has been generated successfully!");
        request.getRequestDispatcher("managerReport.jsp").forward(request, response);
    }

    // Convert GeneratedReport entities to Maps for JSP compatibility
    private List<Map<String, Object>> convertReportsToMaps(List<GeneratedReport> reports) {
        List<Map<String, Object>> reportMaps = new ArrayList<>();
        for (GeneratedReport report : reports) {
            Map<String, Object> reportMap = new HashMap<>();
            reportMap.put("id", report.getId());
            reportMap.put("name", report.getName());
            reportMap.put("date", report.getDate());
            reportMap.put("type", report.getType());
            reportMap.put("status", report.getStatus());
            reportMap.put("size", report.getSize());
            reportMap.put("dateFrom", report.getDateFrom());  // Correct method name with capital F
            reportMap.put("dateTo", report.getDateTo());      // Correct method name with capital T
            reportMap.put("summary1", report.getSummary1());
            reportMap.put("summary2", report.getSummary2());
            reportMap.put("summary3", report.getSummary3());
            reportMap.put("summary4", report.getSummary4());
            reportMap.put("summary5", report.getSummary5());
            reportMaps.add(reportMap);
        }
        return reportMaps;
    }

    // Financial Report Summary using EJB
    private Map<String, Object> getFinancialReportSummary(String dateFrom, String dateTo) {
        Map<String, Object> summary = new LinkedHashMap<>();
        try {
            // Total Revenue
            double totalRevenue = appointmentFacade.getTotalRevenueByDateRange(dateFrom, dateTo);
            summary.put("totalRevenue", totalRevenue);

            // Completed Appointments
            long completedAppointments = appointmentFacade.countCompletedAppointmentsByDateRange(dateFrom, dateTo);
            summary.put("completedAppointments", completedAppointments);

            // Pending Payments
            long pendingPayments = appointmentFacade.countPendingPaymentsByDateRange(dateFrom, dateTo);
            summary.put("pendingPayments", pendingPayments);

            // Average Payment per Visit
            double avgPayment = appointmentFacade.getAveragePaymentByDateRange(dateFrom, dateTo);
            summary.put("avgPayment", avgPayment);

        } catch (Exception e) {
            e.printStackTrace();
            summary.put("error", "EJB error: " + e.getMessage());
        }
        return summary;
    }

    // Staff Performance Summary using EJB
private Map<String, Object> getStaffPerformanceSummary(String dateFrom, String dateTo) {
    Map<String, Object> summary = new LinkedHashMap<>();
    try {
        // Active Doctors (this is a general count, not date-specific)
        long activeDoctors = doctorFacade.countActiveDoctors();
        summary.put("activeDoctors", activeDoctors);

        // Counter Staff (this is a general count, not date-specific)
        long counterStaff = counterStaffFacade.countAllCounterStaff();
        summary.put("counterStaff", counterStaff);

        // Top Doctor (filtered by date range - appointments in the selected period)
        String topDoctor = appointmentFacade.getTopDoctorByDateRange(dateFrom, dateTo);
        summary.put("topDoctor", topDoctor);

        // Top Feedback Score (filtered by date range - feedback in the selected period)
        String topDoctorFeedback = appointmentFacade.getTopFeedbackScore(dateFrom, dateTo);
        summary.put("topDoctorFeedback", topDoctorFeedback);

    } catch (Exception e) {
        e.printStackTrace();
        summary.put("error", "EJB error: " + e.getMessage());
    }
    return summary;
}

    // Appointment Analytics Summary using EJB
    private Map<String, Object> getAppointmentAnalyticsSummary(String dateFrom, String dateTo) {
        Map<String, Object> summary = new LinkedHashMap<>();
        try {
            // Total appointments in range
            long totalAppointments = appointmentFacade.countTotalAppointmentsByDateRange(dateFrom, dateTo);
            summary.put("totalAppointments", totalAppointments);

            // Completed
            long completed = appointmentFacade.countCompletedAppointmentsByDateRange(dateFrom, dateTo);
            summary.put("completed", completed);

            // Scheduled
            long scheduled = appointmentFacade.countScheduledAppointmentsByDateRange(dateFrom, dateTo);
            summary.put("scheduled", scheduled);

            // Most common type
            String mostCommonType = appointmentFacade.getMostCommonTypeByDateRange(dateFrom, dateTo);
            summary.put("mostCommonType", mostCommonType);

            // Feedback summary
            String feedbackSummary = appointmentFacade.getFeedbackSummaryByDateRange(dateFrom, dateTo);
            summary.put("feedbackSummary", feedbackSummary);

        } catch (Exception e) {
            e.printStackTrace();
            summary.put("error", "EJB error: " + e.getMessage());
        }
        return summary;
    }

    private String getReportTypeName(String type) {
        if ("financial".equals(type)) return "Financial Report";
        if ("staff".equals(type)) return "Staff Performance Report";
        if ("appointments".equals(type)) return "Appointment Analytics Report";
        return "Unknown Report";
    }
}