<%@page import="model.Appointment"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ include file="sidebar_customer.jsp" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>


<html>
<head>
    <meta charset="UTF-8">
    <title>Customer - Dashboard</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" />
    <style>
        body { background-color: #f9fafb; }
        .card { border-radius: 0.5rem; }
        .card-header { background: none; border-bottom: none; }
        .stat-icon { padding: 0.5rem; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; }
        .timeline-border { border-left: 4px solid; padding-left: 1rem; }
    </style>
</head>
<body>

<!--Set logged-in name(Greeting)-->
<%
    model.Customer user = (model.Customer) session.getAttribute("user");
    String name = (user != null && user.getName() != null) ? user.getName() : "Guest";
%> 
<%
    java.time.LocalDate today = java.time.LocalDate.now();
%>

<div class="container-fluid" style="margin-left: 260px; padding: 20px; max-width: 1200px;">
    <div class="d-flex justify-content-between align-items-center">
        <h2 class="font-weight-bold">Welcome Back, <%= name %>!</h2>
        <!-- Book Appointment Button -->
        <button class="btn btn-dark mb-3" data-toggle="modal" data-target="#addModal">
            <i class="fas fa-plus mr-1"></i> Book Appointment
        </button>
    </div>
    <p class="text-muted mb-4">Here's your health summary and upcoming appointments.</p>

    <!-- Stats Grid -->
    <div class="row mb-4">
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-calendar-alt fa-2x text-primary mb-2"></i>
                    <h6 class="text-muted">Upcoming Appointments</h6>
                    <h3>${upcomingCount}</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-user-check fa-2x text-success mb-2"></i>
                    <h6 class="text-muted">Completed</h6>
                    <h3>${completedCount}</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-wallet fa-2x text-warning mb-2"></i>
                    <h6 class="text-muted">Outstanding Balance</h6>
                    <h3>0</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-comment-dots fa-2x text-info mb-2"></i>
                    <h6 class="text-muted">Feedback Given</h6>
                    <h3>0</h3>
                </div>
            </div>
        </div>
    </div>

    <!-- Upcoming Appointments and Recent Activity -->
    <div class="card-body">
        <h6 class="text-muted">Upcoming Appointment</h6>
        <c:choose>
            <c:when test="${not empty upcomingAppointments}">
                <c:forEach var="app" items="${upcomingAppointments}">
                    <div class="p-3 mb-2 bg-primary bg-opacity-10 rounded d-flex justify-content-between align-items-center">
                        <div>
                            <div class="font-weight-bold">${app.doctorName}</div>
                            <small class="text-dark">${app.date} at ${app.time}</small><br>
                            <small class="text-dark">${app.type}</small>
                        </div>
                        <form method="post" action="customerCancel" onsubmit="return confirm('Are you sure you want to cancel this appointment?');">
                            <input type="hidden" name="cancelId" value="${app.id}" />
                            <button type="submit" class="btn btn-danger btn-sm">Cancel</button>
                        </form>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p class="text-muted">No upcoming appointments.</p>
            </c:otherwise>
        </c:choose>
    </div>
        
            <div class="col-lg-6 mb-3">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">Recent Activity</h5>
                        <small class="text-muted">Your latest medical history</small>
                    </div>
                    <div class="card-body">
                        <div class="timeline-border border-success mb-3">
                            <div class="font-weight-bold">Consultation Completed</div>
                            <small class="text-muted">Dr. Smith - Cardiology checkup completed successfully</small><br>
                            <small class="text-secondary">3 days ago</small>
                        </div>
                        <div class="timeline-border border-primary mb-3">
                            <div class="font-weight-bold">Payment Processed</div>
                            <small class="text-muted">RM 150 - Consultation fee paid</small><br>
                            <small class="text-secondary">3 days ago</small>
                        </div>
                        <div class="timeline-border border-warning">
                            <div class="font-weight-bold">Feedback Submitted</div>
                            <small class="text-muted">Thank you for rating your experience</small><br>
                            <small class="text-secondary">1 week ago</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>

<!-- Add Appointment Modal -->
<div class="modal fade" id="addModal">
    <div class="modal-dialog">
        <form method="post" action="CustomerDashBoard" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Book Appointment</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label for="doctorName">Select Doctor</label>
                    <select class="form-control" id="doctorName" name="doctorName" required>
                        <option value="Dr. Smith">Dr. Smith (Cardiology)</option>
                        <option value="Dr. Johnson">Dr. Johnson (General Medicine)</option>
                        <option value="Dr. Steve">Dr. Strange (Neurosurgery)</option>
                        <option value="Dr. Lee">Dr. Lee (Orthopaedic)</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="date">Appointment Date</label>
                    <input type="date" class="form-control" id="date" name="date" min="<%= today %>" required />
                </div>
                <div class="form-group">
                    <label for="time">Appointment Time</label>
                    <input type="time" class="form-control" id="time" name="time" required />
                </div>
                <div class="form-group">
                    <label for="type">Type of Appointment</label>
                    <select class="form-control" id="type" name="type" required>
                        <option value="Consultation">Consultation</option>
                        <option value="Follow-up">Follow-up</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="reason">Reason for Visit</label>
                    <textarea class="form-control" id="reason" name="reason" rows="3" required></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-dark">Book Appointment</button>
            </div>
        </form>
    </div>
</div>


                

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>

</body>
</html>
