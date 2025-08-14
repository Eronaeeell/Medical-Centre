package medicalcentre;

import java.io.IOException;
import java.lang.reflect.Field;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.CounterStaff;
import model.CounterStaffFacade;
import model.Doctor;
import model.DoctorFacade;

@WebServlet("/EditStaffServlet")
public class EditStaffServlet extends HttpServlet {

    @EJB private CounterStaffFacade counterStaffFacade;
    @EJB private DoctorFacade doctorFacade;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idStr       = param(request, "id");
        String name        = param(request, "name");
        String email       = param(request, "email");
        String phone       = param(request, "phone");
        String roleParam   = param(request, "role");       // e.g., "COUNTERSTAFF" or "DOCTOR" (or display value)
        String department  = param(request, "department");
        String status      = param(request, "status");
        String joinDate    = param(request, "joinDate");
        String sourceParam = param(request, "source");      // "COUNTERSTAFF" or "DOCTOR"

        // Fallback: if source not provided, infer it from role
        String target = !sourceParam.isEmpty()
                ? sourceParam
                : (roleParam.equalsIgnoreCase("DOCTOR") ? "DOCTOR" : "COUNTERSTAFF");

        boolean updated = false;

        try {
            if ("DOCTOR".equalsIgnoreCase(target)) {
                Doctor d = findDoctorById(idStr);
                if (d != null) {
                    d.setName(name);
                    d.setEmail(email);
                    d.setPhonenumber(phone);
                    d.setDepartment(department);
                    d.setStatus(status);
                    d.setJoinDate(joinDate); // entity uses String
                    doctorFacade.edit(d);
                    updated = true;
                }

            } else if ("COUNTERSTAFF".equalsIgnoreCase(target)) {
                CounterStaff cs = findCounterStaffById(idStr);
                if (cs != null) {
                    cs.setStaffname(name);
                    cs.setEmail(email);
                    cs.setPhoneNo(phone);

                    // Keep your existing role label, or map if you let users edit it
                    if (!roleParam.isEmpty()) {
                        cs.setRole(roleParam);
                    }

                    // No public setter for department -> reflection
                    setPrivateString(cs, "department", department);

                    cs.setStatus(status);
                    cs.setJoinDate(joinDate); // entity uses String
                    counterStaffFacade.edit(cs);
                    updated = true;
                }
            } else {
                // Unknown source/role
            }

            response.getWriter().write(updated ? "success" : "fail");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private static String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }

    private Doctor findDoctorById(String idStr) {
        // Try Integer first, then Long — works with typical JPA id types
        try { Doctor d = doctorFacade.find(Integer.valueOf(idStr)); if (d != null) return d; } catch (Exception ignore) {}
        try { Doctor d = doctorFacade.find(Long.valueOf(idStr));    if (d != null) return d; } catch (Exception ignore) {}
        return null;
    }

    private CounterStaff findCounterStaffById(String idStr) {
        try { CounterStaff c = counterStaffFacade.find(Integer.valueOf(idStr)); if (c != null) return c; } catch (Exception ignore) {}
        try { CounterStaff c = counterStaffFacade.find(Long.valueOf(idStr));    if (c != null) return c; } catch (Exception ignore) {}
        return null;
    }

    private static void setPrivateString(Object obj, String fieldName, String value) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (Exception ignored) {}
    }
}
