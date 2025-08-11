<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ include file="sidebar_counter.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Counter - Appointment Management</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body { background-color: #f9fafb; }
        .table td, .table th { vertical-align: middle; padding: 0.5rem; font-size: 0.9rem; }
        .badge-pill { border-radius: 9999px; padding: 0.35em 0.65em; font-size: 0.75rem; }
        .action-btn { background: none; border: none; cursor: pointer; padding: 0.2rem; }
        .action-btn i { font-size: 1rem; color: #333; }
        .action-btn.reschedule i { color: #17a2b8; }
        .action-btn.edit i { color: #0d6efd; }
        .action-btn:hover i { opacity: 0.8; }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-left: 260px; padding-top: 20px; max-width: 1000px;">
    <h2 class="font-weight-bold">Appointment Management</h2>
    <p class="text-muted mb-3">Book and manage patient appointments</p>

    <!-- Book Appointment Button -->
    <button class="btn btn-dark mb-3" data-toggle="modal" data-target="#addModal">
        <i class="fas fa-plus mr-1"></i> Book Appointment
    </button>

    <!-- Appointment Table -->
    <div class="table-responsive">
        <table class="table table-sm table-hover">
            <thead class="thead-light">
                <tr>
                    <th>Time</th>
                    <th>Patient</th>
                    <th>Doctor</th>
                    <th>Type</th>
                    <th>Status</th>
                    <th>Notes</th>
                    <th style="width: 15%; min-width: 110px;">Actions</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Map<String, String>> appointments = (List<Map<String, String>>) request.getAttribute("appointments");
                int total = 0, completed = 0, pending = 0, confirmed = 0;
                if (appointments != null) {
                    total = appointments.size();
                    for (Map<String, String> appt : appointments) {
                        String status = appt.get("status");
                        if ("completed".equalsIgnoreCase(status)) completed++;
                        else if ("pending".equalsIgnoreCase(status)) pending++;
                        else if ("confirmed".equalsIgnoreCase(status)) confirmed++;

                        String badgeClass =
                            "confirmed".equalsIgnoreCase(status) ? "badge-primary" :
                            "pending".equalsIgnoreCase(status) ? "badge-warning text-dark" :
                            "completed".equalsIgnoreCase(status) ? "badge-success" : "badge-secondary";
            %>
            <tr>
                <td><i class="fas fa-clock text-muted mr-1"></i><%= appt.get("time") %></td>
                <td><i class="fas fa-user text-muted mr-1"></i><%= appt.get("patientName") %></td>
                <td><%= appt.get("doctorName") %></td>
                <td><%= appt.get("type") %></td>
                <td><span class="badge badge-pill <%= badgeClass %>"><%= status %></span></td>
                <td><%= appt.get("notes") %></td>
                <td>
                    <div class="d-flex align-items-center">
                        <button type="button"
                            class="action-btn edit"
                            data-toggle="modal"
                            data-target="#editModal"
                            data-id="<%= appt.get("id") %>"
                            data-patient="<%= appt.get("patientName") %>"
                            data-doctor="<%= appt.get("doctorName") %>"
                            data-date="<%= appt.get("date") %>"
                            data-time="<%= appt.get("time") %>"
                            data-type="<%= appt.get("type") %>"
                            data-status="<%= appt.get("status") %>"
                            data-notes="<%= appt.get("reason") %>">
                            <i class="fas fa-edit"></i>
                        </button>
                    </div>
                </td>
            </tr>
            <% } } %>
            </tbody>
        </table>
    </div>

    <!-- Quick Stats Cards -->
    <div class="row mt-4">
        <div class="col-md-3 col-sm-6 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-calendar fa-2x text-primary mb-2"></i>
                    <h6 class="text-muted">Total Appointment</h6>
                    <h3><%= total %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 col-sm-6 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-clock fa-2x text-success mb-2"></i>
                    <h6 class="text-muted">Completed</h6>
                    <h3><%= completed %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 col-sm-6 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-user fa-2x text-warning mb-2"></i>
                    <h6 class="text-muted">Pending</h6>
                    <h3><%= pending %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 col-sm-6 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-calendar fa-2x" style="color: #800080; margin-bottom: 0.5rem;"></i>
                    <h6 class="text-muted">Confirmed</h6>
                    <h3><%= confirmed %></h3>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Add Appointment Modal -->
<div class="modal fade" id="addModal">
    <div class="modal-dialog">
        <form method="post" action="counterAddAppointment" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Book Appointment</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <%@ include file="counterAppointmentsAdd.jsp" %>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-dark">Book Appointment</button>
            </div>
        </form>
    </div>
</div>
            
<!-- Edit Appointment Modal -->
<div class="modal fade" id="editModal">
    <div class="modal-dialog">
        <form method="post" action="CounterUpdate" class="modal-content">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="id" id="edit-id">
            <div class="modal-header">
                <h5 class="modal-title">Edit Appointment</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <%@ include file="counterAppointmentsEdit.jsp" %>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Update Appointment</button>
            </div>
        </form>
    </div>
</div>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<script>
     $('.action-btn.edit, .action-btn.reschedule').on('click', function () {
        $('#edit-id').val($(this).data('id'));
        $('#edit-patient').val($(this).data('patient'));
        $('#edit-doctor').val($(this).data('doctor'));
        $('#edit-date').val($(this).data('date'));
        $('#edit-time').val($(this).data('time'));
        $('#edit-type').val($(this).data('type'));
        $('#edit-status').val($(this).data('status'));
        $('#edit-notes').val($(this).data('notes'));
    });
</script>
</body>
</html>
