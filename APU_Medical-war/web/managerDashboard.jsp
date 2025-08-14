<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="sidebar_manager.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manager - Dashboard</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body { background-color: #f9fafb; }
        .card { border-radius: 0.5rem; }
        .card-header { background: none; border-bottom: none; }
        .stat-icon {
            padding: 0.5rem;
            border-radius: 50%;
            display: inline-flex;
            align-items: center;
            justify-content: center;
        }
        .timeline-border { border-left: 4px solid; padding-left: 1rem; }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-left: 260px; padding: 20px; max-width: 1000px;">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0 d-flex align-items-center">
            <i class="fas fa-tachometer-alt fa-lg text-primary mr-2"></i>
            Manager Dashboard
        </h4>
    </div>
    <p class="text-muted mb-4">Welcome back! Here's what's happening at APU Medical Centre.</p>

    <!-- Summary Cards -->
    <div class="row mb-4">
        <div class="col-md-3 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <div class="stat-icon bg-primary bg-opacity-10 mb-2">
                        <i class="fas fa-users fa-lg text-primary"></i>
                    </div>
                    <h6 class="text-muted">Total Staff</h6>
                    <h4>${totalStaff}</h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <div class="stat-icon bg-success bg-opacity-10 mb-2">
                        <i class="fas fa-calendar-check fa-lg text-success"></i>
                    </div>
                    <h6 class="text-muted">Today's Appointments</h6>
                    <h4>${todaysAppointments}</h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <div class="stat-icon bg-purple bg-opacity-10 mb-2" style="background-color: #f3e8ff;">
                        <i class="fas fa-coins fa-lg text-purple" style="color: #6f42c1;"></i>
                    </div>
                    <h6 class="text-muted">Monthly Revenue</h6>
                    <h4>RM ${monthlyRevenue}</h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <div class="stat-icon bg-warning bg-opacity-10 mb-2">
                        <i class="fas fa-file-alt fa-lg text-warning"></i>
                    </div>
                    <h6 class="text-muted">Reports Generated</h6>
                    <h4>${reportsGenerated}</h4>
                    <small class="text-muted">This month</small>
                </div>
            </div>
        </div>
    </div>

    

        <div class="col-lg-6 mb-3">
            <div class="card shadow-sm">
                <div class="card-header">
                    <h5 class="mb-0">System Overview</h5>
                    <small class="text-muted">Key metrics and system health</small>
                </div>
                <div class="card-body">
                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted">Active Doctors</span>
                        <span class="font-weight-bold">${activeDoctors}</span>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted">Counter Staff</span>
                        <span class="font-weight-bold">${counterStaff}</span>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted">Registered Customers</span>
                        <span class="font-weight-bold">${registeredCustomers}</span>
                    </div>
                    <div class="d-flex justify-content-between">
                        <span class="text-muted">Pending Appointments</span>
                        <span class="font-weight-bold">${pendingAppointments}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>
