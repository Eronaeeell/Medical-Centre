<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*,java.text.*" %>
<%@ include file="sidebar_doctor.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Doctor - Appointments</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body { background-color: #f9fafb; }
        .card-stat { border-radius: 0.5rem; }
        .card-stat .card-body { padding: 1.5rem 1rem; }
        .stat-icon { padding: 0.7rem; border-radius: 50%; font-size: 1.5rem; }
        .table thead { background-color: #f0f2f5; }
        .table td, .table th { vertical-align: middle; font-size: 0.9rem; }
        .badge-pill { border-radius: 9999px; padding: 0.35em 0.65em; font-size: 0.75rem; }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-left: 260px; padding: 20px; max-width: 1100px;">
    <div class="mb-4">
        <h4 class="font-weight-bold mb-0">
            <i class="fas fa-calendar-check mr-2"></i> My Appointments
        </h4>
        <p class="text-muted mb-3">View and manage your assigned patient appointments below.</p>
    </div>

    <!-- Summary Cards -->
    <%
        List<Map<String, Object>> appointments = (List<Map<String, Object>>) request.getAttribute("allAppointments");
        int todayCount = request.getAttribute("todayCount") != null ? (Integer) request.getAttribute("todayCount") : 0;
        int completedToday = request.getAttribute("completedTodayCount") != null ? (Integer) request.getAttribute("completedTodayCount") : 0;
        int inProgressToday = request.getAttribute("inProgressTodayCount") != null ? (Integer) request.getAttribute("inProgressTodayCount") : 0;
        int upcomingCount = request.getAttribute("upcomingCount") != null ? (Integer) request.getAttribute("upcomingCount") : 0;
    %>
    <div class="row mb-4">
        <div class="col-md-3 mb-2">
            <div class="card card-stat shadow-sm text-center">
                <div class="card-body">
                    <span class="stat-icon bg-primary text-white"><i class="fas fa-calendar-day"></i></span>
                    <h6 class="text-muted mt-2">Today's Appointments</h6>
                    <h4><%= todayCount %></h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-2">
            <div class="card card-stat shadow-sm text-center">
                <div class="card-body">
                    <span class="stat-icon bg-success text-white"><i class="fas fa-check-circle"></i></span>
                    <h6 class="text-muted mt-2">Completed Today</h6>
                    <h4><%= completedToday %></h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-2">
            <div class="card card-stat shadow-sm text-center">
                <div class="card-body">
                    <span class="stat-icon bg-warning text-white"><i class="fas fa-user-clock"></i></span>
                    <h6 class="text-muted mt-2">In Progress</h6>
                    <h4><%= inProgressToday %></h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-2">
            <div class="card card-stat shadow-sm text-center">
                <div class="card-body">
                    <span class="stat-icon bg-info text-white"><i class="fas fa-calendar-plus"></i></span>
                    <h6 class="text-muted mt-2">Upcoming</h6>
                    <h4><%= upcomingCount %></h4>
                </div>
            </div>
        </div>
    </div>

    <!-- Appointment Table -->
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-striped table-hover mb-0">
                    <thead>
                        <tr>
                            <th>Time</th>
                            <th>Patient</th>
                            <th>Type</th>
                            <th>Reason</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        if (appointments != null && !appointments.isEmpty()) {
                            for (Map<String, Object> a : appointments) {
                                String status = (String) a.get("status");
                    %>
                        <tr>
                            <td>
                                <span class="font-weight-bold"><%= a.get("appointmentTime") %></span><br>
                                <small class="text-muted"><%= a.get("appointmentDate") %></small>
                            </td>
                            <td>
                                <span class="font-weight-bold"><%= a.get("patientName") %></span><br>
                                <small class="text-muted">
                                    <%= a.get("patientAge") %>y, <%= a.get("patientGender") %>
                                </small>
                            </td>
                            <td>
                                <span class="badge badge-info badge-pill"><%= a.get("appointmentType") %></span>
                            </td>
                            <td>
                                <span title="<%= a.get("reason") %>">
                                    <%= a.get("reason") %>
                                </span>
                            </td>
                            <td>
                                <% if ("Confirmed".equals(status)) { %>
                                    <span class="badge badge-primary badge-pill">Confirmed</span>
                                <% } else if ("In Progress".equals(status)) { %>
                                    <span class="badge badge-warning badge-pill text-dark">In Progress</span>
                                <% } else if ("Completed".equals(status)) { %>
                                    <span class="badge badge-success badge-pill">Completed</span>
                                <% } else if ("Scheduled".equals(status)) { %>
                                    <span class="badge badge-info badge-pill">Scheduled</span>
                                <% } else if ("Cancelled".equals(status)) { %>
                                    <span class="badge badge-danger badge-pill">Cancelled</span>
                                <% } else { %>
                                    <span class="badge badge-secondary badge-pill"><%= status %></span>
                                <% } %>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="5" class="text-center text-muted">No appointments found.</td>
                        </tr>
                    <%
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>
