import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.ejb.EJB;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import model.CounterStaff;
import model.CounterStaffFacade;
import model.Doctor;
import model.DoctorFacade;

@WebServlet("/ManagerStaffServlet")
public class ManagerStaffServlet extends HttpServlet {

    @EJB private CounterStaffFacade counterStaffFacade;
    @EJB private DoctorFacade doctorFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Separate lists for two tables
        List<Map<String, String>> counterStaffList = new ArrayList<>();
        List<Map<String, String>> doctorList = new ArrayList<>();

        // (Optional) merged list preserved for backwards-compat
        List<Map<String, String>> staffList = new ArrayList<>();

        int totalStaff = 0, activeStaff = 0, inactiveStaff = 0;
        Set<String> departments = new HashSet<>();

        try {
            // -------- COUNTER STAFF via EJB --------
            for (CounterStaff cs : counterStaffFacade.findAll()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", cs.getId() == null ? "" : cs.getId().toString());
                row.put("name", nz(cs.getStaffname()));
                row.put("email", nz(cs.getEmail()));
                row.put("phone", nz(cs.getPhoneNo()));
                row.put("role", nz(cs.getRole()));
                String dept = nz(getPrivateString(cs, "department")); // no public getter
                row.put("department", dept);
                String status = nz(cs.getStatus());
                row.put("status", status);
                row.put("joinDate", nz(cs.getJoinDate())); // String in your entity
                row.put("source", "COUNTERSTAFF");

                counterStaffList.add(row);
                staffList.add(row);

                totalStaff++;
                if ("Active".equalsIgnoreCase(status)) activeStaff++; else inactiveStaff++;
                if (!dept.isEmpty()) departments.add(dept);
            }

            // -------- DOCTOR via EJB --------
            for (Doctor d : doctorFacade.findAll()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", d.getId() == null ? "" : d.getId().toString());
                row.put("name", nz(d.getName()));
                row.put("email", nz(d.getEmail()));
                row.put("phone", nz(d.getPhonenumber()));
                row.put("role", "Doctor"); // normalize for JSP
                String dept = nz(d.getDepartment());
                row.put("department", dept);
                String status = nz(d.getStatus());
                row.put("status", status);
                row.put("joinDate", nz(d.getJoinDate())); // String in your entity
                row.put("source", "DOCTOR");

                doctorList.add(row);
                staffList.add(row);

                totalStaff++;
                if ("Active".equalsIgnoreCase(status)) activeStaff++; else inactiveStaff++;
                if (!dept.isEmpty()) departments.add(dept);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Attributes for JSP
        request.setAttribute("counterStaffList", counterStaffList);
        request.setAttribute("doctorList", doctorList);
        request.setAttribute("staffList", staffList); // keep for any other page usage
        request.setAttribute("totalStaff", totalStaff);
        request.setAttribute("activeStaff", activeStaff);
        request.setAttribute("inactiveStaff", inactiveStaff);
        request.setAttribute("totalDepartments", departments.size());

        request.getRequestDispatcher("managerStaff.jsp").forward(request, response);
    }

    private static String nz(String s) { return s == null ? "" : s; }

    // read private field (department) without changing EJB
    private static String getPrivateString(Object obj, String fieldName) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            Object v = f.get(obj);
            return v == null ? "" : String.valueOf(v);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
