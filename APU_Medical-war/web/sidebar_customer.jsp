<%
    String activePage = (String) request.getAttribute("activePage");
%>

<div class="d-flex flex-column flex-shrink-0 p-3 bg-light" style="width: 250px; height: 100vh; position: fixed;">
    <a href="CustomerDashboardServlet" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-dark text-decoration-none">
        <span class="fs-4">APU Medical - Customer</span>
    </a>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <a href="CustomerDashBoard"
               class="nav-link d-flex align-items-center <%= "dashboard".equals(activePage) ? "active-link" : "text-dark" %>">
                Dashboard
            </a>
        </li>
        <li>
            <a href="customerAppointmentsServlet"
               class="nav-link d-flex align-items-center <%= "appointments".equals(activePage) ? "active-link" : "text-dark" %>">
                Appointments
            </a>
        </li>
        <li>
            <a href="customerPayment"
               class="nav-link d-flex align-items-center <%= "payments".equals(activePage) ? "active-link" : "text-dark" %>">
                Payments
            </a>
        </li>
        <li>
            <a href="customerFeedback"
               class="nav-link d-flex align-items-center <%= "feedback".equals(activePage) ? "active-link" : "text-dark" %>">
                Feedback
            </a>
        </li>
    </ul>
    <hr>

    <!-- ? Profile button ABOVE Logout -->
    <div class="mb-2">
        <button type="button" class="btn btn-outline-primary btn-sm w-100"
                data-toggle="modal" data-target="#profileModal">
            Profile
        </button>
    </div>

    <!-- Logout button -->
    <div>
        <a href="#" class="btn btn-outline-danger w-100" onclick="confirmLogout()">Logout</a>
    </div>
    <script>
      function confirmLogout() {
          if (confirm('Are you sure you want to logout?')) {
              window.location.href = 'LogoutServlet';
          }
      }
    </script>
</div>

<!-- Include the modal markup OUTSIDE the fixed sidebar -->
<jsp:include page="profile.jsp" />

<style>
  .active-link {
      background-color:#e7f1ff;
      color:#0d6efd!important;
      border-radius:.375rem;
      font-weight:500;
  }
  .nav-link:hover {
      background-color:#f0f4f8;
      color:#0a58ca!important;
  }
</style>
