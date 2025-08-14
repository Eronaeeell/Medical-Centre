import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.CounterStaff;
import model.CounterStaffFacade;
import model.Doctor;
import model.DoctorFacade;
import model.Manager;
import model.ManagerFacade;

@WebServlet(urlPatterns = {"/staffLoginServlet"})
public class staffLoginServlet extends HttpServlet {

    @EJB
    private DoctorFacade doctorFacade;

    @EJB
    private CounterStaffFacade counterStaffFacade;
    
    @EJB 
    private ManagerFacade managerFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            HttpSession session = request.getSession();

            // COUNTER
            if ("counter".equals(role)) {
                CounterStaff counter = counterStaffFacade.findByEmail(email);
                if (counter != null && counter.getPassword().equals(password)) {
                    session.setAttribute("counter", counter);
                    response.sendRedirect("counterDashboard.jsp"); // make sure file exists
                    return;
                }
            }
            // DOCTOR (this must NOT be inside the counter block)
            else if ("doctor".equals(role)) {
                Doctor doctor = doctorFacade.findByEmail(email);
                if (doctor != null && doctor.getPassword().equals(password)) {
                    session.setAttribute("doctor", doctor);
                    response.sendRedirect("doctorDashBoard.jsp"); // or doctorDashboard.jsp, pick one and keep it
                    return;
                }
            }
            // === MANAGER ===
            else if ("manager".equalsIgnoreCase(role)) {
                Manager manager = managerFacade.findByEmail(email);
                if (manager != null && password.equals(manager.getPassword())) {
                    // Optional status check: comment out if you don't store status yet
                    // if (manager.getStatus() != null && !"ACTIVE".equalsIgnoreCase(manager.getStatus())) {
                    //     response.sendRedirect("staff_login.jsp?error=Your+account+is+inactive.");
                    //     return;
                    // }

                    session.setAttribute("manager", manager);
                    // Even if not implemented yet, redirect is correct once JSP exists
                    response.sendRedirect("managerDashboard.jsp");
                    return;
                }
            }

            // Invalid login → send back to the SAME login JSP name you actually use
            response.sendRedirect("staff_login.jsp?error=Invalid email, password, or role.");
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
