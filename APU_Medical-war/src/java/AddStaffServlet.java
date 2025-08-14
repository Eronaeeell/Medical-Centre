import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.CounterStaff;
import model.CounterStaffFacade;
import model.Doctor;
import model.DoctorFacade;

@WebServlet("/AddStaffServlet")
public class AddStaffServlet extends HttpServlet {

    @EJB
    private CounterStaffFacade counterStaffFacade;

    @EJB
    private DoctorFacade doctorFacade;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name       = param(request, "name");
        String email      = param(request, "email");
        String phone      = param(request, "phone");
        String roleParam  = param(request, "role");        // "COUNTERSTAFF" or "DOCTOR"
        String department = param(request, "department");
        String status     = param(request, "status");
        String joinDate   = param(request, "joinDate");    // Entities use String
        String password   = param(request, "password");    // NEW

        try {
            if ("DOCTOR".equalsIgnoreCase(roleParam)) {
                // ---- Insert into DOCTOR table ----
                Doctor d = new Doctor();
                d.setName(name);
                d.setEmail(email);
                d.setPhonenumber(phone);
                d.setDepartment(department);
                d.setStatus(status);
                d.setJoinDate(joinDate);

                // Set password flexibly (setter or field)
                setPasswordFlexible(d, password);

                doctorFacade.create(d);

            } else if ("COUNTERSTAFF".equalsIgnoreCase(roleParam)) {
                // ---- Insert into COUNTERSTAFF table ----
                CounterStaff cs = new CounterStaff();
                cs.setStaffname(name);
                cs.setEmail(email);
                cs.setPhoneNo(phone);
                cs.setRole("Counter Staff"); // for display consistency

                // department has no public setter in your entity -> reflection
                setPrivateString(cs, "department", department);

                cs.setStatus(status);
                cs.setJoinDate(joinDate);

                // Set password flexibly (setter or field)
                setPasswordFlexible(cs, password);

                counterStaffFacade.create(cs);

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid role selected.");
                return;
            }

            // Redirect back to listing so the new row appears
            response.sendRedirect(request.getContextPath() + "/ManagerStaffServlet");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("managerStaff.jsp").forward(request, response);
        }
    }

    private static String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }

    // Try calling setPassword(String), else write a private "password" field.
    private static void setPasswordFlexible(Object obj, String rawPassword) {
        if (rawPassword == null) rawPassword = "";
        // TODO (recommended): hash rawPassword before storing.
        try {
            // Prefer a public setter if present
            Method m = obj.getClass().getMethod("setPassword", String.class);
            m.invoke(obj, rawPassword);
            return;
        } catch (NoSuchMethodException ignore) {
            // fall through to field write
        } catch (Exception e) {
            // if setter exists but failed, try field write as fallback
        }
        setPrivateString(obj, "password", rawPassword);
    }

    // Write a private field safely
    private static void setPrivateString(Object obj, String fieldName, String value) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (Exception ignored) {}
    }
}
