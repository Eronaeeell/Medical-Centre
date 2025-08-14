<%-- Sidebar.jsp (include this in your layout pages) --%>
<%
    // Prefer request attribute set by Servlet; fallback to URL inference
    String activePage = (String) request.getAttribute("activePage");
    if (activePage == null) {
        String uri = request.getRequestURI();
        if (uri.contains("ManagerDashboard")) activePage = "dashboard";
        else if (uri.contains("ManagerStaff")) activePage = "staffManagement";
        else if (uri.contains("ManagerAppointments")) activePage = "appointments";
        else if (uri.contains("ManagerReport")) activePage = "report";
    }
%>

<div class="d-flex flex-column flex-shrink-0 p-3 bg-light sidebar">
    <a href="ManagerDashboardServlet" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-dark text-decoration-none">
        <span class="fs-4">APU Medical - Manager</span>
    </a>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <a href="ManagerDashboardServlet"
               class="nav-link d-flex align-items-center <%= "dashboard".equals(activePage) ? "active-link" : "text-dark" %>">
                Dashboard
            </a>
        </li>
        <li>
            <a href="ManagerStaffServlet"
               class="nav-link d-flex align-items-center <%= "staffManagement".equals(activePage) ? "active-link" : "text-dark" %>">
                Staff Management
            </a>
        </li>
        <li>
            <a href="ManagerAppointmentsServlet"
               class="nav-link d-flex align-items-center <%= "appointments".equals(activePage) ? "active-link" : "text-dark" %>">
                Appointments
            </a>
        </li>
        <li>
            <a href="ManagerReportServlet"
               class="nav-link d-flex align-items-center <%= "report".equals(activePage) ? "active-link" : "text-dark" %>">
                Report
            </a>
        </li>
    </ul>
    <hr>
    <div>
        <a href="LogoutServlet" 
           class="btn btn-outline-danger w-100"
           onclick="return confirmLogout();">
            Logout
        </a>
        <script>
            function confirmLogout() {
                return confirm('Are you sure you want to logout?');
            }
        </script>
    </div>
</div>

<style>
    /* Sidebar is fixed; content will sit to its right */
    .sidebar {
        width: 250px;
        height: 100vh;
        position: fixed;
        left: 0;
        top: 0;
        overflow-y: auto;
    }

    /* Push main content to the right so it's not hidden */
    body { margin-left: 250px; }

    .active-link {
        background-color: #e7f1ff; /* Light blue background */
        color: #0d6efd !important; /* Bootstrap primary blue */
        border-radius: 0.375rem;
        font-weight: 500;
    }

    .nav-link:hover,
    .btn:hover {
        background-color: #f0f4f8; /* Lighter hover */
        color: #0a58ca !important; /* Slightly darker blue on hover */
    }
</style>
