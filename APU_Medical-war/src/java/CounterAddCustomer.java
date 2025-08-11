import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Customer;
import model.CustomerFacade;

@WebServlet(urlPatterns = {"/CounterAddCustomer"})
public class CounterAddCustomer extends HttpServlet {

    @EJB
    private CustomerFacade customerFacade;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
    
        

    // Get form data
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    String phoneNo = request.getParameter("phoneNo");
    String ic = request.getParameter("ic");
    String age = request.getParameter("age");
    String gender = request.getParameter("gender");
    String password = "123"; // default

    // Create customer
    Customer c = new Customer(name, ic, age, gender, email, phoneNo, password);
    customerFacade.create(c);

    // Redirect (do not include JSP after redirect)
    response.sendRedirect("CounterCustomerServlet?success=Customer+added+successfully");
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
        
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phoneNo = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String age = request.getParameter("age");
        String ic = request.getParameter("ic");
        String password = "123"; //initiate password
        
        
        Customer c = new Customer(name, ic, age, gender, email, phoneNo, password);
        // Redirect with success
        request.getSession().setAttribute("success", "Customer added successfully.");
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
