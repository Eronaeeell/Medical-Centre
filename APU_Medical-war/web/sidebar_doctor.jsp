<%
    String activePage = (String) request.getAttribute("activePage");
%>
<div class="d-flex flex-column flex-shrink-0 p-3 bg-light" style="width: 250px; height: 100vh; position: fixed;">
    <a href="DoctorDashboardServlet" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-dark text-decoration-none">
        <span class="fs-4">APU Medical - Doctor</span>
    </a>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <a href="DoctorDashboardServlet"
               class="nav-link d-flex align-items-center <%= "dashboard".equals(activePage) ? "active-link" : "text-dark" %>">
                Dashboard
            </a>
        </li>
        <li>
            <a href="DoctorAppointmentsServlet"
               class="nav-link d-flex align-items-center <%= "appointments".equals(activePage) ? "active-link" : "text-dark" %>">
                Appointments
            </a>
        </li>
        <li>
            <a href="DoctorChargesServlet"
               class="nav-link d-flex align-items-center <%= "charges".equals(activePage) ? "active-link" : "text-dark" %>">
                Charges
            </a>
        </li>
        <li>
            <a href="DoctorHistoryServlet"
               class="nav-link d-flex align-items-center <%= "history".equals(activePage) ? "active-link" : "text-dark" %>">
                History
            </a>
        </li>
    </ul>
    <hr>
    <div>
        <a href="#" class="btn btn-outline-danger w-100" onclick="confirmLogout()">Logout</a>
        <script>
        function confirmLogout() {
            if (confirm('Are you sure you want to logout?')) {
                window.location.href = 'LogoutServlet';
            }
        }
        </script>
    </div>
</div>
<style>
    .active-link {
        background-color: #e7f1ff; /* Light blue background */
        color: #0d6efd !important;
        border-radius: 0.375rem;
        font-weight: 500;
    }
    .nav-link:hover {
        background-color: #f0f4f8;
        color: #0a58ca !important;
    }
</style>
    