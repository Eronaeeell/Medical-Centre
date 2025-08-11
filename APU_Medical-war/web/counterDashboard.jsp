<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="sidebar_counter.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Counter Dashboard - APU Medical Centre</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body>
    <div style="margin-left:250px; padding: 20px;">
        <h1 class="mb-3">Counter Dashboard</h1>
        <p class="text-muted">Manage customer appointments and payments efficiently.</p>

        <div class="row">
            <div class="col-md-3 mb-3">
                <div class="card bg-primary text-white">
                    <div class="card-body">
                        <h5>Today's Customers</h5>
                        <h3><%= request.getAttribute("todaysCustomers") %></h3>
                        <small>+8 from yesterday</small>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card bg-success text-white">
                    <div class="card-body">
                        <h5>Appointments Booked</h5>
                        <h3><%= request.getAttribute("appointmentsBooked") %></h3>
                        <small>Today</small>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card bg-info text-white">
                    <div class="card-body">
                        <h5>Payments Collected</h5>
                        <h3><%= request.getAttribute("paymentsCollected") %></h3>
                        <small>Today</small>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card bg-warning text-dark">
                    <div class="card-body">
                        <h5>Pending Tasks</h5>
                        <h3><%= request.getAttribute("pendingTasks") %></h3>
                        <small>Require attention</small>
                    </div>
                </div>
            </div>
        </div>

        <!-- You can add Today's Appointments and Recent Payments sections here -->
    </div>
</body>
</html>
