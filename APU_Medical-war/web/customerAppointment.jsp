<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ include file="sidebar_customer.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="model.Appointment" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer - Appointments</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" />
    <style>
        body { background-color: #f9fafb; }
        .card { border-radius: 0.5rem; }
        .badge-status { font-size: 0.8rem; }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-left: 260px; padding: 20px; max-width: 1000px;">
    <h2 class="font-weight-bold">My Medical History</h2>
    <p class="text-muted mb-4">View your appointment history and payment records</p>

    <!-- Summary Cards with Icons -->
    
    <div class="row mb-4">
    <div class="col-md-3 mb-3">
        <div class="card shadow-sm text-center">
            <div class="card-body">
                <i class="fas fa-calendar-alt fa-2x text-primary mb-2"></i>
                <h6 class="text-muted">Upcoming Appointments</h6>
                <h3><c:out value="${upcomingCount}" /></h3>
            </div>  
        </div>
    </div>
    <div class="col-md-3 mb-3">
        <div class="card shadow-sm text-center">
            <div class="card-body"> 
                <i class="fas fa-check-circle fa-2x text-success mb-2"></i>
                <h6 class="text-muted">Completed</h6>
                <h3><c:out value="${completedCount}" /></h3>
            </div>
        </div>
    </div>
    <div class="col-md-3 mb-3">
        <div class="card shadow-sm text-center">
            <div class="card-body">
                <i class="fas fa-wallet fa-2x text-warning mb-2"></i>
                <h6 class="text-muted">Total Paid</h6>
                <h3><c:out value="${totalPaid}" /></h3>
            </div>
        </div>
    </div>
    <div class="col-md-3 mb-3">
        <div class="card shadow-sm text-center">
            <div class="card-body">
                <i class="fas fa-hourglass-half fa-2x text-info mb-2"></i>
                <h6 class="text-muted">Upcoming</h6>
                <h3>${upcomingAppointments}</h3>
            </div>
        </div>
    </div>
</div>

    <!-- Display Appointments -->
<%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
%>
<%
    java.time.LocalDate today = java.time.LocalDate.now();
%>
        <!-- Appointment Card -->
        
        
        <div class="card mb-3">
            <% if (appointments == null || appointments.isEmpty()) { %>
    <div class="card mb-3">
        <div class="card-body text-center">
            <p class="text-muted">No appointments scheduled yet.</p>
        </div>
    </div>
            
<% } else { %>
    <% for (Appointment appt : appointments) { %>
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title"><i class="fas fa-user-md mr-2"></i> Dr. <%= appt.getDoctorName() %></h5>
                <p class="card-text mb-1"><strong>Type:</strong> <%= appt.getType() %></p>
                <p class="card-text mb-1"><strong>Date:</strong> <%= appt.getDate() %></p>
                <p class="card-text mb-0"><strong>Time:</strong> <%= appt.getTime() %></p>
                <p class="card-text mb-0"><strong>Status:</strong> <%= appt.getStatus() %></p>
            </div>
        </div>
    <% } %>
<% } %>
        </div>

    </div>

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
                <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-dark">Book Appointment</button>
            </div>
        </form>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>


</body>
</html>
