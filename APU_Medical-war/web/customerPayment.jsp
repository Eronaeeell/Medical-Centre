<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.Payment" %>
<%@ include file="sidebar_customer.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer - Payments</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body { background-color: #f9fafb; }
        .table td, .table th { vertical-align: middle; padding: 0.5rem; font-size: 0.9rem; }
        .badge-pill { border-radius: 9999px; padding: 0.35em 0.65em; font-size: 0.75rem; }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-left: 260px; padding-top: 20px; max-width: 1200px;">
    <h2 class="font-weight-bold">Payment Collection</h2>
    <p class="text-muted mb-3">View your payment history and outstanding charges</p>

    <%
        // Retrieve attributes from servlet as previously set in the servlet 
        List<Payment> payments = (List<Payment>) request.getAttribute("payments");
        List<Map<String, String>> pendingCharges = (List<Map<String, String>>) request.getAttribute("pendingCharges");
        List<Map<String, String>> paymentHistory = (List<Map<String, String>>) request.getAttribute("paymentHistory");
        
        // Initiate value for count the reciept and outstanding
        int receiptsIssued = 0;
        double outstanding = 0;

        // ✅ Count receipts only for this logged-in customer by using paymentHistory
        if (paymentHistory != null) {
            for (Map<String, String> ph : paymentHistory) {
                if ("Paid".equalsIgnoreCase(ph.get("type"))) receiptsIssued++;
            }
        }

        if (pendingCharges != null) {
            for (Map<String, String> ch : pendingCharges) {
                outstanding += Double.parseDouble(ch.get("amount"));
            }
        }
    %>

    <!-- Payment Stats Cards -->
    <div class="row mb-4">
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-dollar-sign fa-2x text-success mb-2"></i>
                    <h6 class="text-muted">Today's Collection</h6>
                    <h3>RM<%= request.getAttribute("totalCollectedToday") %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-receipt fa-2x text-primary mb-2"></i>
                    <h6 class="text-muted">Receipts Issued</h6>
                    <h3><%= receiptsIssued %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-credit-card fa-2x text-purple mb-2"></i>
                    <h6 class="text-muted">Pending Payments</h6>
                    <h3><%= (pendingCharges != null ? pendingCharges.size() : 0) %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-money-bill fa-2x text-warning mb-2"></i>
                    <h6 class="text-muted">Outstanding</h6>
                    <h3>RM <%= outstanding %></h3>
                </div>
            </div>
        </div>
    </div>

    <!-- Pending Charges -->
    <h5 class="font-weight-bold">Pending Charges</h5>
    <div class="table-responsive mb-4">
        <table class="table table-sm table-hover">
            <thead class="thead-light">
                <tr>
                    <th>Patient</th>
                    <th>Doctor</th>
                    <th>Date</th>
                    <th>Services</th>
                    <th>Amount</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <% if (pendingCharges != null && !pendingCharges.isEmpty()) {
                for (Map<String, String> pc : pendingCharges) { %>
                <tr>
                    <td><%= pc.get("patientName") %></td>
                    <td><%= pc.get("doctorName") %></td>
                    <td><%= pc.get("date") %></td>
                    <td><%= pc.get("services") %></td>
                    <td>RM <%= pc.get("amount") %></td>
                    <td>
                        <button type="button" class="btn btn-sm btn-success pay-btn"
                            data-toggle="modal" data-target="#payModal"
                            data-appointment="<%= pc.get("appointmentId") %>"
                            data-patient="<%= pc.get("patientName") %>"
                            data-doctor="<%= pc.get("doctorName") %>"
                            data-date="<%= pc.get("date") %>"
                            data-services="<%= pc.get("services") %>"
                            data-amount="<%= pc.get("amount") %>">
                            Collect Payment
                        </button>
                    </td>
                </tr>
            <% } } else { %>
                <tr>
                    <td colspan="6" class="text-center text-muted">No pending charges available</td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <!-- Payment History -->
    <h5 class="font-weight-bold">Payment History</h5>
    <div class="table-responsive">
        <table class="table table-sm table-hover">
            <thead class="thead-light">
                <tr>
                    <th>Patient</th>
                    <th>Doctor</th>
                    <th>Services</th>
                    <th>Amount</th>
                    <th>Payment Method</th>
                    <th>Status</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
            <% if (paymentHistory != null && !paymentHistory.isEmpty()) {
                for (Map<String, String> ph : paymentHistory) { %>
                <tr>
                    <td><%= ph.get("patientName") %></td>
                    <td><%= ph.get("doctorName") %></td>
                    <td><%= ph.get("services") %></td>
                    <td>RM <%= ph.get("amount") %></td>
                    <td><%= ph.get("method") %></td>
                    <td>
                        <span class="badge badge-pill <%= "Paid".equalsIgnoreCase(ph.get("type")) ? "badge-success" : "badge-warning" %>">
                            <%= ph.get("type") %>
                        </span>
                    </td>
                    <td><%= ph.get("paymentDate") %> <%= ph.get("paymentTime") %></td>
                </tr>
            <% } } else { %>
                <tr>
                    <td colspan="8" class="text-center text-muted">No payment history available</td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<!-- Pay Modal -->
<div class="modal fade" id="payModal">
    <div class="modal-dialog">
        <form method="post" action="customerPayment" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Process Payment</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="action" value="pay">
                <input type="hidden" id="modal-appointmentId" name="appointmentId">
                <input type="hidden" id="modal-patient" name="patientName">
                <input type="hidden" id="modal-doctor" name="doctorName">
                <input type="hidden" id="modal-date" name="date">
                <input type="hidden" id="modal-services" name="services">
                <input type="hidden" id="modal-amount" name="amount">

                <p><strong>Patient:</strong> <span id="modal-patient-view"></span></p>
                <p><strong>Doctor:</strong> <span id="modal-doctor-view"></span></p>
                <p><strong>Date:</strong> <span id="modal-date-view"></span></p>
                <p><strong>Services:</strong> <span id="modal-services-view"></span></p>
                <p><strong>Amount:</strong> RM <span id="modal-amount-view"></span></p>

                <div class="form-group">
                    <label for="paymentMethod">Payment Method</label>
                    <select id="paymentMethod" name="method" class="form-control" required>
                        <option value="">Select payment method</option>
                        <option value="Cash">Cash</option>
                        <option value="Card">Card</option>
                        <option value="Online Banking">Online Banking</option>
                        <option value="Insurance">Insurance</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="notes">Notes (Optional)</label>
                    <textarea id="notes" name="notes" class="form-control" rows="3" placeholder="Any notes..."></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-success">Process Payment</button>
            </div>
        </form>
    </div>
</div>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<script>
    $('.pay-btn').on('click', function () {
        $('#modal-appointmentId').val($(this).data('appointment'));
        $('#modal-patient').val($(this).data('patient'));
        $('#modal-doctor').val($(this).data('doctor'));
        $('#modal-date').val($(this).data('date'));
        $('#modal-services').val($(this).data('services'));
        $('#modal-amount').val($(this).data('amount'));

        $('#modal-patient-view').text($(this).data('patient'));
        $('#modal-doctor-view').text($(this).data('doctor'));
        $('#modal-date-view').text($(this).data('date'));
        $('#modal-services-view').text($(this).data('services'));
        $('#modal-amount-view').text($(this).data('amount'));
    });
</script>
</body>
</html>
