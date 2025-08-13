<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ include file="sidebar_doctor.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Doctor - Dashboard</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" />
    <style>
        body { background-color: #f9fafb; }
        .card { border-radius: 0.5rem; }
        .card-header { background: none; border-bottom: none; }
        .stat-icon {display: inline-flex; align-items: center; justify-content: center; border-radius: 50%; font-size: 1.5rem; margin-bottom: 0.5rem;}
        .timeline-border { border-left: 4px solid; padding-left: 1rem; }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-left: 260px; padding: 20px; max-width: 1200px;">
    <div class="d-flex justify-content-between align-items-center">
        <h2 class="font-weight-bold">Welcome Back, Dr. Lee!</h2>
        
    </div>
    <p class="text-muted mb-4">Here's your clinic summary and schedule overview.</p>

    <!-- Stats Grid -->
    <div class="row mb-4">
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-calendar-alt fa-2x text-primary mb-2"></i>
                    <h6 class="text-muted">Today's Appointments</h6>
                    <h3>5</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-user-injured fa-2x text-success mb-2"></i>
                    <h6 class="text-muted">Total Patients</h6>
                    <h3>47</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-clipboard-list fa-2x text-warning mb-2"></i>
                    <h6 class="text-muted">Pending Reports</h6>
                    <h3>2</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-comment-medical fa-2x text-info mb-2"></i>
                    <h6 class="text-muted">Recent Feedback</h6>
                    <h3>8</h3>
                </div>
            </div>
        </div>
    </div>

    <!-- Upcoming Appointments and Patient Notes -->
    <div class="row">
        <div class="col-lg-6 mb-3">
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">Upcoming Appointments</h5>
                    <small class="text-muted">Your scheduled consultations</small>
                </div>
                <div class="card-body">
                    <div class="p-3 mb-2 bg-primary bg-opacity-10 rounded d-flex justify-content-between align-items-center">
                        <div>
                            <div class="font-weight-bold">John Doe - Chest Pain</div>
                            <small class="text-dark">Today, 10:00 AM</small><br>
                            <small class="text-dark">Consultation</small>
                        </div>
                        <button class="btn btn-primary btn-sm">View</button>
                    </div>
                    <div class="p-3 bg-success bg-opacity-10 rounded d-flex justify-content-between align-items-center">
                        <div>
                            <div class="font-weight-bold">Alice Brown - Diabetes Checkup</div>
                            <small class="text-dark">Today, 2:00 PM</small><br>
                            <small class="text-dark">Follow-up</small>
                        </div>
                        <button class="btn btn-success btn-sm">View</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-6 mb-3">
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">Recent Patient Notes</h5>
                    <small class="text-muted">Your latest case updates</small>
                </div>
                <div class="card-body">
                    <div class="timeline-border border-success mb-3">
                        <div class="font-weight-bold">Follow-up Advice Given</div>
                        <small class="text-muted">Patient Alice - Diabetes management plan updated</small><br>
                        <small class="text-secondary">10 minutes ago</small>
                    </div>
                    <div class="timeline-border border-primary mb-3">
                        <div class="font-weight-bold">Prescription Issued</div>
                        <small class="text-muted">Patient John Doe - Prescribed new medication</small><br>
                        <small class="text-secondary">30 minutes ago</small>
                    </div>
                    <div class="timeline-border border-warning">
                        <div class="font-weight-bold">Lab Result Reviewed</div>
                        <small class="text-muted">Patient Mike - Blood test reviewed</small><br>
                        <small class="text-secondary">1 hour ago</small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Add Patient Note Modal -->
<div class="modal fade" id="addNoteModal">
    <div class="modal-dialog">
        <form method="post" action="DoctorDashboardServlet" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add Patient Note</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label for="patientName">Patient Name</label>
                    <input type="text" class="form-control" id="patientName" name="patientName" required>
                </div>
                <div class="form-group">
                    <label for="noteType">Note Type</label>
                    <select class="form-control" id="noteType" name="noteType" required>
                        <option value="Consultation">Consultation</option>
                        <option value="Follow-up">Follow-up</option>
                        <option value="Prescription">Prescription</option>
                        <option value="Lab Result">Lab Result</option>
                        <option value="Other">Other</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="details">Details</label>
                    <textarea class="form-control" id="details" name="details" rows="3" required></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-dark">Add Note</button>
            </div>
        </form>
    </div>
</div>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>


