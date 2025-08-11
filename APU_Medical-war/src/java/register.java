 import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Customer;
import model.CustomerFacade;

@WebServlet(urlPatterns = {"/register"})
public class register extends HttpServlet {

    @EJB
    private CustomerFacade customerFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try{
                // Retrieve the data from user ( mostly from the form in jsp) 
                String name = request.getParameter("name");
                String ic = request.getParameter("ic");
                String age = request.getParameter("age");
                String gender = request.getParameter("gender");
                String email = request.getParameter("email");
                String phoneNo = request.getParameter("phoneNo");
                String password = request.getParameter("password");
                
            if (name == null || !name.matches("[A-Za-z]+")) {
                request.setAttribute("error", "Name must contain letters only.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Validate age (digits only)
            if (age == null || !age.matches("\\d+")) {
                request.setAttribute("error", "Age must be a number.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Validate phone (digits only)
            if (phoneNo == null || !phoneNo.matches("\\d+")) {
                request.setAttribute("error", "Phone number must be numeric.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            
            // create/insert a new table in the model (CUSTOMER) 
            Customer c = new Customer(name, ic, age, gender, email, phoneNo, password);
            customerFacade.create(c);
            request.getRequestDispatcher("login.jsp").include(request,response);
            
            } catch (Exception e){
                request.getRequestDispatcher("register.jsp").include(request,response);
            }
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
    