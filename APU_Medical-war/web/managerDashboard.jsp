<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="model.Staff" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manager - Dashboard</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" />
    <style>
        body { background-color: #f9fafb; }
        .sidebar {
            width: 250px;
            height: 100vh;
            position: fixed;
            top: 0;
            left: 0;
            background: #343a40;
            padding: 20px 0;
        }
        .sidebar h4 {
            color: white;
            text-align: center;
            margin-bottom: 30px;
        }
        .sidebar a {
            color: #adb5bd;
            text-decoration: none;
            display: block;
            padding: 15px 20px;
            border-bottom: 1px solid #495057;
        }
        .sidebar a:hover {
            color: white;
            background: #495057;
            text-decoration: none;
        }
        .main-content {
            margin-left: 250px;
            padding: 20px;
        }
        .card { border-radius: 0.5rem; }
        .card-header { background: none; border-bottom: none; }
        .stat-icon {display: inline-flex; align-items: center; justify-content: center; border-radius: 50%; font-size: 1.5rem; margin-bottom: 0.5rem;}
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h4><i class="fas fa-user-tie"></i> Manager Panel</h4>
        <a href="ManagerDashboardServlet"><i class="fas fa-tachometer-alt"></i> Dashboard</a>
        <a href="#"><i class="fas fa-users"></i> Staff Management</a>
        <a href="#"><i class="fas fa-chart-bar"></i> Reports</a>
        <a href="#"><i class="fas fa-cogs"></i> Settings</a>
        <a href="LogoutServlet"><i class="fas fa-sign-out-alt"></i> Logout</a>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <%
            Staff manager = (Staff) request.getAttribute("manager");
            String managerName = (manager != null) ? manager.getName() : "Manager";
        %>
        <div class="d-flex justify-content-between align-items-center">
            <h2 class="font-weight-bold">Welcome Back, <%= managerName %>!</h2>
        </div>
        <p class="text-muted mb-4">Here's your medical center overview and key statistics.</p>

        <!-- Stats Grid -->
        <div class="row mb-4">
            <div class="col-md-3 mb-3">
                <div class="card shadow-sm text-center">
                    <div class="card-body">
                        <i class="fas fa-users fa-2x text-primary mb-2"></i>
                        <h6 class="text-muted">Total Staff</h6>
                        <h3><%= request.getAttribute("totalStaff") %></h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card shadow-sm text-center">
                    <div class="card-body">
                        <i class="fas fa-calendar-check fa-2x text-success mb-2"></i>
                        <h6 class="text-muted">Today's Appointments</h6>
                        <h3><%= request.getAttribute("todaysAppointments") %></h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card shadow-sm text-center">
                    <div class="card-body">
                        <i class="fas fa-dollar-sign fa-2x text-warning mb-2"></i>
                        <h6 class="text-muted">Monthly Revenue</h6>
                        <h3><%= request.getAttribute("monthlyRevenue") %></h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card shadow-sm text-center">
                    <div class="card-body">
                        <i class="fas fa-file-alt fa-2x text-info mb-2"></i>
                        <h6 class="text-muted">Reports Generated</h6>
                        <h3><%= request.getAttribute("reportsGenerated") %></h3>
                    </div>
                </div>
            </div>
        </div>

        <!-- Second Row of Stats -->
        <div class="row mb-4">
            <div class="col-md-3 mb-3">
                <div class="card shadow-sm text-center">
                    <div class="card-body">
                        <i class="fas fa-user-md fa-2x text-primary mb-2"></i>
                        <h6 class="text-muted">Active Doctors</h6>
                        <h3><%= request.getAttribute("activeDoctors") %></h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card shadow-sm text-center">
                    <div class="card-body">
                        <i class="fas fa-desktop fa-2x text-secondary mb-2"></i>
                        <h6 class="text-muted">Counter Staff</h6>
                        <h3><%= request.getAttribute("counterStaffCount") %></h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card shadow-sm text-center">
                    <div class="card-body">
                        <i class="fas fa-user-friends fa-2x text-success mb-2"></i>
                        <h6 class="text-muted">Registered Customers</h6>
                        <h3><%= request.getAttribute("registeredCustomers") %></h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card shadow-sm text-center">
                    <div class="card-body">
                        <i class="fas fa-clock fa-2x text-warning mb-2"></i>
                        <h6 class="text-muted">Pending Appointments</h6>
                        <h3><%= request.getAttribute("pendingAppointments") %></h3>
                    </div>
                </div>
            </div>
        </div>

        <!-- Quick Actions and Summary -->
        <div class="row">
            <div class="col-lg-6 mb-3">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">Quick Actions</h5>
                        <small class="text-muted">Manage your medical center</small>
                    </div>
                    <div class="card-body">
                        <div class="p-3 mb-2 bg-primary bg-opacity-10 rounded d-flex justify-content-between align-items-center">
                            <div>
                                <div class="font-weight-bold">Staff Management</div>
                                <small class="text-dark">Manage doctors and counter staff</small>
                            </div>
                            <button class="btn btn-primary btn-sm">Manage</button>
                        </div>
                        <div class="p-3 mb-2 bg-success bg-opacity-10 rounded d-flex justify-content-between align-items-center">
                            <div>
                                <div class="font-weight-bold">Generate Reports</div>
                                <small class="text-dark">Create monthly/quarterly reports</small>
                            </div>
                            <button class="btn btn-success btn-sm">Generate</button>
                        </div>
                        <div class="p-3 bg-warning bg-opacity-10 rounded d-flex justify-content-between align-items-center">
                            <div>
                                <div class="font-weight-bold">System Settings</div>
                                <small class="text-dark">Configure system parameters</small>
                            </div>
                            <button class="btn btn-warning btn-sm">Configure</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-6 mb-3">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">System Overview</h5>
                        <small class="text-muted">Current system status</small>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <div class="d-flex justify-content-between">
                                <span>Staff Utilization</span>
                                <span class="text-success">Good</span>
                            </div>
                            <div class="progress" style="height: 8px;">
                                <div class="progress-bar bg-success" style="width: 75%"></div>
                            </div>
                        </div>
                        <div class="mb-3">
                            <div class="d-flex justify-content-between">
                                <span>Appointment Load</span>
                                <span class="text-warning">Moderate</span>
                            </div>
                            <div class="progress" style="height: 8px;">
                                <div class="progress-bar bg-warning" style="width: 60%"></div>
                            </div>
                        </div>
                        <div>
                            <div class="d-flex justify-content-between">
                                <span>Revenue Target</span>
                                <span class="text-primary">On Track</span>
                            </div>
                            <div class="progress" style="height: 8px;">
                                <div class="progress-bar bg-primary" style="width: 85%"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>