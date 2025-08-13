import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Customer;
import model.CustomerFacade;

@WebServlet(urlPatterns = {"/CustomerProfile"})
public class CustomerProfile extends HttpServlet {

    @EJB
    private CustomerFacade customerFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // We are not writing any body; we only redirect/forward
        // response.setContentType("text/html;charset=UTF-8");

//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("user") == null) {
//            response.sendRedirect(request.getContextPath() + "/login.jsp?message=Please+log+in");
//            return;
//        }
//
//        // If it's not a POST, just send to dashboard (no profile view here)
//        if (!"POST".equalsIgnoreCase(request.getMethod())) {
//            response.sendRedirect(request.getContextPath() + "/CustomerDashBoard");
//            return;
//        }
//
//        // Only accept the expected action
//        String action = request.getParameter("action");
//        if (action != null && !"updateProfile".equalsIgnoreCase(action)) {
//            response.sendRedirect(request.getContextPath() + "/CustomerDashBoard");
//            return;
//        }
//
//        Customer loggedIn = (Customer) session.getAttribute("user");
//
//        // Anti-tamper: posted id must match session user's id
//        String postedId = (request.getParameter("id"));
//        if (postedId == null || !postedId.equals(String.valueOf(loggedIn.getId()))) {
//            session.setAttribute("error", "Invalid profile update request.");
//            response.sendRedirect(request.getContextPath() + "/CustomerDashBoard");
//            return;
//        }
//
//        // Only fields allowed to change
//        String email   = request.getParameter("email");
//        String phoneNo = request.getParameter("phoneNo");
//        phoneNo = phoneNo = request.getParameter("phone"); // fallback if input named "phone"
//
//        // Validate inputs
//        if (email != null && !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$")) {
//            session.setAttribute("error", "Invalid email format.");
//            response.sendRedirect(request.getContextPath() + "/CustomerDashBoard");
//            return;
//        }
//        if (phoneNo != null && !phoneNo.matches("^[0-9+][0-9\\-\\s]{6,}$")) {
//            session.setAttribute("error", "Invalid phone number.");
//            response.sendRedirect(request.getContextPath() + "/CustomerDashBoard");
//            return;
//        }
//
//        try {
//            // Load, update, persist
//            Customer db = customerFacade.find(loggedIn.getId());
//            if (db == null) {
//                session.setAttribute("error", "Account not found.");
//                response.sendRedirect(request.getContextPath() + "/CustomerDashBoard");
//                return;
//            }
//
//            if (email != null)   db.setEmail(email);
//            if (phoneNo != null) setPhoneFlexible(db, phoneNo); // handles setPhoneNo / setPhone / setPhoneNumber
//
//            customerFacade.edit(db);
//
//            // Refresh session user & flash message
//            session.setAttribute("user", db);
//            session.setAttribute("success", "Profile updated successfully.");
//        } catch (Exception ex) {
//            session.setAttribute("error", "Failed to update profile: " + ex.getMessage());
//        }
//
//        // PRG: redirect after POST
//        response.sendRedirect(request.getContextPath() + "/CustomerDashBoard");
//    }
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
