<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ include file="sidebar_customer.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer - Feedback & Reviews</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body { background-color: #f9fafb; }
        .table td, .table th { vertical-align: middle; font-size: 0.9rem; }
        .badge-pill { border-radius: 9999px; padding: 0.35em 0.65em; font-size: 0.75rem; }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-left: 260px; padding-top: 20px; max-width: 1200px;">
    <h2 class="font-weight-bold">Feedback & Reviews</h2>
    <p class="text-muted mb-3">Share your experience and help us improve our services</p>

    <%
        List<Map<String, String>> pendingFeedback = (List<Map<String, String>>) request.getAttribute("pendingFeedback");
        List<Map<String, String>> feedbackHistory = (List<Map<String, String>>) request.getAttribute("feedbackHistory");
        int totalFeedback = feedbackHistory != null ? feedbackHistory.size() : 0;
        int pendingCount = pendingFeedback != null ? pendingFeedback.size() : 0;
        int reviewedCount = 0;
        double avgRating = 0.0;

        if (feedbackHistory != null && !feedbackHistory.isEmpty()) {
            double totalRating = 0;
            for (Map<String, String> fb : feedbackHistory) {
                if ("Reviewed".equalsIgnoreCase(fb.get("status"))) reviewedCount++;
                totalRating += Double.parseDouble(fb.get("doctorRating"));
            }
            avgRating = totalRating / feedbackHistory.size();
        }
    %>

    <!-- Feedback Stats Cards -->
    <div class="row mb-4">
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-comment fa-2x text-primary mb-2"></i>
                    <h6 class="text-muted">Total Feedback</h6>
                    <h3><%= totalFeedback %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-star fa-2x text-warning mb-2"></i>
                    <h6 class="text-muted">Avg. Doctor Rating</h6>
                    <h3><%= String.format("%.1f", avgRating) %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-hourglass-half fa-2x text-success mb-2"></i>
                    <h6 class="text-muted">Pending Feedback</h6>
                    <h3><%= pendingCount %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-check-circle fa-2x text-purple mb-2"></i>
                    <h6 class="text-muted">Reviewed</h6>
                    <h3><%= reviewedCount %></h3>
                </div>
            </div>
        </div>
    </div>

    <!-- Pending Feedback Table -->
    <h5 class="font-weight-bold">Appointments Awaiting Feedback</h5>
    <div class="table-responsive mb-4">
        <table class="table table-sm table-hover">
            <thead class="thead-light">
                <tr>
                    <th>Date</th>
                    <th>Doctor</th>
                    <th>Counter Staff</th>
                    <th>Type</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <% if (pendingFeedback != null && !pendingFeedback.isEmpty()) {
                for (Map<String, String> apt : pendingFeedback) { %>
                <tr>
                    <td><%= apt.get("date") %></td>
                    <td><%= apt.get("doctorName") %></td>
                    <td><%= apt.get("counterStaff") %></td>
                    <td><%= apt.get("type") %></td>
                    <td>
                        <button type="button" class="btn btn-sm btn-primary feedback-btn"
                            data-toggle="modal" data-target="#feedbackModal"
                            data-id="<%= apt.get("id") %>"
                            data-date="<%= apt.get("date") %>"
                            data-doctor="<%= apt.get("doctorName") %>"
                            data-counter="<%= apt.get("counterStaff") %>"
                            data-type="<%= apt.get("type") %>">
                            <i class="fas fa-plus mr-1"></i> Give Feedback
                        </button>
                    </td>
                </tr>
            <% } } else { %>
                <tr>
                    <td colspan="5" class="text-center text-muted">No appointments awaiting feedback</td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <!-- Feedback History Table -->
    <h5 class="font-weight-bold">My Feedback History</h5>
    <div class="table-responsive">
        <table class="table table-sm table-hover">
            <thead class="thead-light">
                <tr>
                    <th>Date</th>
                    <th>Doctor</th>
                    <th>Counter Staff</th>
                    <th>Rating</th>
                    <th>Experience</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
            <% if (feedbackHistory != null && !feedbackHistory.isEmpty()) {
                for (Map<String, String> fb : feedbackHistory) {
                    String status = fb.get("status");
                    String badgeClass =
                        "Reviewed".equalsIgnoreCase(status) ? "badge-success" :
                        "Submitted".equalsIgnoreCase(status) ? "badge-primary" : "badge-secondary";
            %>
                <tr>
                    <td><%= fb.get("appointmentDate") %></td>
                    <td><%= fb.get("doctorName") %></td>
                    <td><%= fb.get("counterStaff") %></td>
                    <td><%= fb.get("doctorRating") %> / 5</td>
                    <td><%= fb.get("overallExperience") %></td>
                    <td><span class="badge badge-pill <%= badgeClass %>"><%= status %></span></td>
                </tr>
            <% } } else { %>
                <tr>
                    <td colspan="6" class="text-center text-muted">No feedback history available</td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<!-- Feedback Modal -->
<div class="modal fade" id="feedbackModal">
    <div class="modal-dialog modal-lg">
        <form method="post" action="customerFeedback" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Submit Feedback</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="action" value="submitFeedback">
                <input type="hidden" id="modal-id" name="id">

                <p><strong>Date:</strong> <span id="modal-date-view"></span></p>
                <p><strong>Doctor:</strong> <span id="modal-doctor-view"></span></p>
                <p><strong>Counter Staff:</strong> <span id="modal-counter-view"></span></p>
                <p><strong>Type:</strong> <span id="modal-type-view"></span></p>

                <div class="form-group">
                    <label for="doctorRating">Doctor Rating (1-5)</label>
                    <input type="number" class="form-control" name="doctorRating" id="doctorRating" min="1" max="5" required>
                </div>

                <div class="form-group">
                    <label for="counterStaffRating">Counter Staff Rating (1-5)</label>
                    <input type="number" class="form-control" name="counterStaffRating" id="counterStaffRating" min="1" max="5" required>
                </div>

                <div class="form-group">
                    <label for="overallexperience">Overall Experience</label>
                    <select class="form-control" name="overallexperience" id="overallExperience" required>
                        <option value="">Select</option>
                        <option value="Excellent">Excellent</option>
                        <option value="Good">Good</option>
                        <option value="Average">Average</option>
                        <option value="Poor">Poor</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="doctorFeedback">Doctor Feedback</label>
                    <textarea class="form-control" name="doctorFeedback" id="doctorFeedback" rows="3" required></textarea>
                </div>

                <div class="form-group">
                    <label for="staffFeedback">Counter Staff Feedback</label>
                    <textarea class="form-control" name="staffFeedback" id="counterstaffFeedback" rows="3" required></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Submit Feedback</button>
            </div>
        </form>
    </div>
</div>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<script>
    $('.feedback-btn').on('click', function () {
        $('#modal-id').val($(this).data('id'));
        $('#modal-date-view').text($(this).data('date'));
        $('#modal-doctor-view').text($(this).data('doctor'));
        $('#modal-counter-view').text($(this).data('counter'));
        $('#modal-type-view').text($(this).data('type'));
    });
</script>
</body>
</html>