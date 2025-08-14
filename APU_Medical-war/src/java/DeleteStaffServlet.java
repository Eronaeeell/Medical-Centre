import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.CounterStaff;
import model.CounterStaffFacade;
import model.Doctor;
import model.DoctorFacade;

@WebServlet("/DeleteStaffServlet")
public class DeleteStaffServlet extends HttpServlet {

    @EJB private CounterStaffFacade counterStaffFacade;
    @EJB private DoctorFacade doctorFacade;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam     = nz(request.getParameter("id"));
        String sourceParam = nz(request.getParameter("source")).toUpperCase(); // "COUNTERSTAFF" or "DOCTOR"

        if (idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing staff ID");
            return;
        }
        if (!"COUNTERSTAFF".equals(sourceParam) && !"DOCTOR".equals(sourceParam)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid source");
            return;
        }

        boolean deleted = false;

        try {
            if ("DOCTOR".equals(sourceParam)) {
                Doctor d = findDoctorById(idParam);
                if (d != null) {
                    doctorFacade.remove(d); // JPA remove
                    deleted = true;
                }
            } else { // COUNTERSTAFF
                CounterStaff cs = findCounterStaffById(idParam);
                if (cs != null) {
                    counterStaffFacade.remove(cs); // JPA remove
                    deleted = true;
                }
            }

            if (deleted) {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("success");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Staff not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error deleting staff: " + e.getMessage());
        }
    }

    private Doctor findDoctorById(String idStr) {
        try { Doctor d = doctorFacade.find(Integer.valueOf(idStr)); if (d != null) return d; } catch (Exception ignore) {}
        try { Doctor d = doctorFacade.find(Long.valueOf(idStr));    if (d != null) return d; } catch (Exception ignore) {}
        return null;
    }

    private CounterStaff findCounterStaffById(String idStr) {
        try { CounterStaff c = counterStaffFacade.find(Integer.valueOf(idStr)); if (c != null) return c; } catch (Exception ignore) {}
        try { CounterStaff c = counterStaffFacade.find(Long.valueOf(idStr));    if (c != null) return c; } catch (Exception ignore) {}
        return null;
    }

    private static String nz(String s) { return s == null ? "" : s.trim(); }
}
