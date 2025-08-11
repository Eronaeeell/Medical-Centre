<%@ page import="java.util.*" %>
<%@ include file="sidebar_counter.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="model.Customer" %>
<%@ page import="model.Appointment" %>
<!DOCTYPE html>
      
<html>
<head>
    <meta charset="UTF-8">
    <title>Counter - Customer Management</title>    
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" />

    <style>
        body { background-color: #f9fafb; }
        .table td, .table th { vertical-align: middle; padding: 0.5rem; font-size: 0.9rem; }
        .badge-pill { border-radius: 9999px; padding: 0.35em 0.65em; font-size: 0.75rem; }
        .action-btn { background: none; border: none; cursor: pointer; padding: 0.2rem; }
        .action-btn i { font-size: 1rem; color: #333; }
        .action-btn.delete i { color: #dc3545; }
        .action-btn:hover i { color: #0d6efd; }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-left: 260px; padding-top: 20px; max-width: 1000px;">
    <h2 class="font-weight-bold">Customer Management</h2>
    <p class="text-muted mb-3">Manage customer information and records</p>

    <!-- Search -->
    <form method="get" action="CounterCustomerSearch" class="form-inline mb-3">
    <input type="text" name="search" class="form-control mr-2" placeholder="Search customers..." style="flex:1; max-width:250px;">
    <button type="submit" class="btn btn-primary mr-2">Search</button>
    <a href="CounterCustomerServlet" class="btn btn-secondary">Reset</a> <!-- Reset Button -->
</form>

    <!-- Add Button -->
    <button class="btn btn-dark mb-3" data-toggle="modal" data-target="#addModal">
        <i class="fas fa-plus mr-1"></i> Add Customer
    </button>

    <!-- Customer Table -->
    <div class="table-responsive">
        <table class="table table-sm table-hover">
            <thead class="thead-light">
                <tr>
                    <th>Name & IC</th>
                    <th>Contact</th>
                    <th>Age/Gender</th>
                    <th>Last Visit</th>
                    <th>Identification</th>
                    <th style="width: 12%; min-width: 90px;">Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Customer> customers = (List<Customer>) request.getAttribute("customers");
                    Map<Long, String> latestStatusMap = (Map<Long, String>) request.getAttribute("latestStatusMap");
                    
                    if (customers != null) {
                        for (Customer c : customers) {
                %>
                <tr>
                    <td>
                        <strong><%= c.getName() %></strong><br>
                        <small class="text-muted"><%= c.getIc() %></small>
                    </td>
                    <td>
                        <div><i class="fas fa-envelope text-muted mr-1"></i><small><%= c.getEmail() %></small></div>
                        <div><i class="fas fa-phone text-muted mr-1"></i><small><%= c.getPhoneNo() %></small></div>
                    </td>
                    <td><%= c.getAge() %> years<br><small class="text-muted"><%= c.getGender() %></small></td>
                    <td></td>
                    <td></td>
                    <td>
                        <div class="d-flex align-items-center">
                            <button type="button"
                                    class="action-btn editBtn"
                                    data-toggle="modal"
                                    data-target="#editModal"
                                    data-name="<%= c.getName() %>"
                                    data-email="<%= c.getEmail() %>"
                                    data-phone="<%= c.getPhoneNo() %>"
                                    data-age="<%= c.getAge() %>"
                                    data-gender="<%= c.getGender() %>"
                                    data-ic="<%= c.getIc() %>">
                                <i class="fas fa-edit"></i>
                            </button>
                            <a href="CounterCustomerServlet?deleteId=<%= c.getId() %>"
                               class="action-btn delete"
                               onclick="return confirm('Are you sure you want to delete this customer?');">
                                <i class="fas fa-trash-alt"></i>
                            </a>
                        </div>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
                
            </tbody>
        </table>
    </div>
</div>

<!-- Add Customer Modal -->
<div class="modal fade" id="addModal">
    <div class="modal-dialog">
        <form method="post" action="CounterAddCustomer" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add Customer</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <%@ include file="counterCustomersAdd.jsp" %>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-dark">Add Customer</button>
            </div>
        </form>
    </div>
</div>

<!-- Edit Customer Modal -->
<div class="modal fade" id="editModal">
    <div class="modal-dialog">
        <form method="post" action="CounterUpdateCustomer" class="modal-content">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="id" id="edit-id">
            <div class="modal-header">
                <h5 class="modal-title">Edit Customer</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <%@ include file="counterCustomersEdit.jsp" %>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Update Customer</button>
            </div>
        </form>
    </div>
</div>

<!-- Scripts -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<script>
     $('.edit-btn').on('click', function() {
      $('#edit-id').val($(this).data('id'));
      $('#edit-name').val($(this).data('name'));
      $('#edit-email').val($(this).data('email'));
      $('#edit-phone').val($(this).data('phone'));
      $('#edit-age').val($(this).data('age'));
      $('#edit-gender').val($(this).data('gender'));
      $('#edit-ic').val($(this).data('ic'));
  });
</script>
</body>
</html>

