<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ include file="sidebar_manager.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manager - Appointments</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        body { background-color: #f9fafb; }
        .table thead { background-color: #f0f2f5; }
        .table td, .table th { vertical-align: middle; font-size: 0.9rem; }
        .badge-pill { border-radius: 9999px; padding: 0.35em 0.65em; font-size: 0.75rem; }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-left: 260px; padding: 20px; max-width: 1000px;">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0 text-primary">
            <i class="bi bi-calendar-check mr-2"></i> Appointments & Feedback Review
        </h4>
    </div>
    <p class="text-muted mb-3">Monitor all appointments and customer feedback (from Appointment entity).</p>

    <!-- Summary Cards (3 only) -->
    <div class="row mb-4">
        <div class="col-md-4 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="bi bi-calendar-check fa-2x text-primary mb-2"></i>
                    <h6 class="text-muted">Total Appointments</h6>
                    <h4><%= request.getAttribute("totalAppointments") %></h4>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="bi bi-check2-circle fa-2x text-success mb-2"></i>
                    <h6 class="text-muted">Completed</h6>
                    <h4><%= request.getAttribute("completedAppointments") %></h4>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="bi bi-chat-left-text fa-2x text-info mb-2"></i>
                    <h6 class="text-muted">With Feedback</h6>
                    <h4><%= request.getAttribute("appointmentsWithFeedback") %></h4>
                </div>
            </div>
        </div>
    </div>

    <!-- Appointments Table (only requested columns) -->
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-striped table-hover mb-0">
                    <thead>
                        <tr>
                            <th>Customer ID</th>     <!-- Patient (CUSTOMER_ID) -->
                            <th>Doctor</th>          <!-- DOCTORNAME -->
                            <th>Date & Time</th>     <!-- DATE + TIME -->
                            <th>Type</th>            <!-- TYPE -->
                            <th>Status</th>          <!-- STATUS -->
                            <th>Payment</th>         <!-- PAYMENT -->
                            <th>Feedback</th>        <!-- FEEDBACK -->
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Map<String, Object>> appointments =
                                (List<Map<String, Object>>) request.getAttribute("appointments");
                            if (appointments != null && !appointments.isEmpty()) {
                                for (Map<String, Object> a : appointments) {
                                    String status   = (String) a.get("status");
                                    String payment  = (String) a.get("payment");
                                    String feedback = (String) a.get("feedback");
                        %>
                        <tr>
                            <td><%= a.get("customerId") %></td>
                            <td><%= a.get("doctorName") %></td>
                            <td>
                                <%= a.get("date") %><br>
                                <small class="text-muted"><%= a.get("time") %></small>
                            </td>
                            <td><%= a.get("type") %></td>
                            <td>
                                <% if ("Completed".equalsIgnoreCase(status)) { %>
                                    <span class="badge badge-success badge-pill">Completed</span>
                                <% } else if ("Scheduled".equalsIgnoreCase(status)) { %>
                                    <span class="badge badge-primary badge-pill">Scheduled</span>
                                <% } else { %>
                                    <span class="badge badge-secondary badge-pill"><%= status %></span>
                                <% } %>
                            </td>
                            <td>
                                <% if ("Paid".equalsIgnoreCase(payment)) { %>
                                    <span class="badge badge-success badge-pill">Paid</span>
                                <% } else if ("Pending".equalsIgnoreCase(payment)) { %>
                                    <span class="badge badge-warning badge-pill text-dark">Pending</span>
                                <% } else { %>
                                    <span class="badge badge-secondary badge-pill"><%= payment %></span>
                                <% } %>
                            </td>
                            <td>
                                <% if (feedback != null && !feedback.trim().isEmpty()) { %>
                                    <span class="badge badge-info badge-pill"><%= feedback %></span>
                                <% } else { %>
                                    <span class="text-muted">No feedback</span>
                                <% } %>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="7" class="text-center text-muted">No appointments found.</td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
