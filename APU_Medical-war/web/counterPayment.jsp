<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ include file="sidebar_counter.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Counter - Payment Collection</title>
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
<div class="container-fluid" style="margin-left:260px; padding-top:20px; max-width:1000px;">
    <h2 class="font-weight-bold">Payment Collection</h2>
    <p class="text-muted mb-3">Process payments and generate receipts for patients</p>

    <%
        List<Map<String, String>> payments = (List<Map<String, String>>) application.getAttribute("payments");
        List<Map<String, String>> pendingCharges = (List<Map<String, String>>) application.getAttribute("pendingCharges");
        double todaysCollection = 2450.0;
        double outstanding = 0.0;
        int receiptsIssued = 0;
        int pendingPayments = pendingCharges != null ? pendingCharges.size() : 0;
        if (payments != null) {
            for (Map<String, String> p : payments) {
                if ("Paid".equalsIgnoreCase(p.get("status"))) receiptsIssued++;
            }
        }
        if (pendingCharges != null) {
            for (Map<String, String> c : pendingCharges) {
                outstanding += Double.parseDouble(c.get("totalAmount"));
            }
        }
    %>

    <!-- Payment Statistics -->
    <div class="row mb-4">
        <div class="col-md-3 col-sm-6 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-dollar-sign fa-2x text-success mb-2"></i>
                    <h6 class="text-muted">Today's Collection</h6>
                    <h3>RM <%= String.format("%.2f", todaysCollection) %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 col-sm-6 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-receipt fa-2x text-primary mb-2"></i>
                    <h6 class="text-muted">Receipts Issued</h6>
                    <h3><%= receiptsIssued %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 col-sm-6 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-credit-card fa-2x text-purple mb-2"></i>
                    <h6 class="text-muted">Pending Payments</h6>
                    <h3><%= pendingPayments %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 col-sm-6 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-dollar-sign fa-2x text-warning mb-2"></i>
                    <h6 class="text-muted">Outstanding</h6>
                    <h3>RM <%= String.format("%.2f", outstanding) %></h3>
                </div>
            </div>
        </div>
    </div>

    <!-- Pending Payments Table -->
    <h5 class="mb-2"><i class="fas fa-credit-card mr-1"></i> Pending Payments</h5>
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
            <%
                if (pendingCharges != null) {
                    for (Map<String, String> c : pendingCharges) {
            %>
            <tr>
                <td><%= c.get("patientName") %></td>
                <td><%= c.get("doctorName") %></td>
                <td><%= c.get("appointmentDate") %></td>
                <td><%= c.get("services") %></td>
                <td>RM <%= c.get("totalAmount") %></td>
                <td>
                    <button type="button" class="btn btn-sm btn-success collect-btn" data-toggle="modal" data-target="#collectModal"
                        data-patient="<%= c.get("patientName") %>"
                        data-doctor="<%= c.get("doctorName") %>"
                        data-services="<%= c.get("services") %>"
                        data-amount="<%= c.get("totalAmount") %>">
                        Collect Payment
                    </button>
                </td>
            </tr>
            <% } } %>
            </tbody>
        </table>
    </div>

    <!-- Payment History Table -->
    <h5 class="mb-2"><i class="fas fa-history mr-1"></i> Payment History</h5>
    <div class="table-responsive mb-4">
        <table class="table table-sm table-hover">
            <thead class="thead-light">
                <tr>
                    <th>Receipt No.</th>
                    <th>Patient</th>
                    <th>Doctor</th>
                    <th>Services</th>
                    <th>Amount</th>
                    <th>Payment Method</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <%
                if (payments != null) {
                    for (Map<String, String> p : payments) {
                        String status = p.get("status");
                        String badgeClass =
                            "Paid".equalsIgnoreCase(status) ? "badge-success" :
                            "Pending".equalsIgnoreCase(status) ? "badge-warning text-dark" :
                            "Refunded".equalsIgnoreCase(status) ? "badge-danger" : "badge-secondary";
            %>
            <tr>
                <td><%= p.get("receiptNo") %></td>
                <td><%= p.get("patientName") %></td>
                <td><%= p.get("doctorName") %></td>
                <td><%= p.get("services") %></td>
                <td>RM <%= p.get("amount") %></td>
                <td><%= p.get("paymentMethod") %></td>
                <td><span class="badge badge-pill <%= badgeClass %>"><%= status %></span></td>
                <td>
                    <button class="btn btn-sm btn-outline-primary print-btn">
                        <i class="fas fa-print"></i>
                    </button>
                </td>
            </tr>
            <% } } %>
            </tbody>
        </table>
    </div>
</div>

<!-- Collect Payment Modal -->
<div class="modal fade" id="collectModal">
    <div class="modal-dialog">
        <form method="post" action="CounterPaymentServlet" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Process Payment</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="action" value="collect">
                <input type="hidden" id="modal-patient" name="patientName">
                <input type="hidden" id="modal-doctor" name="doctorName">
                <input type="hidden" id="modal-services" name="services">
                <input type="hidden" id="modal-amount" name="amount">

                <p><strong>Patient:</strong> <span id="modal-patient-view"></span></p>
                <p><strong>Doctor:</strong> <span id="modal-doctor-view"></span></p>
                <p><strong>Services:</strong> <span id="modal-services-view"></span></p>
                <p><strong>Amount:</strong> RM <span id="modal-amount-view"></span></p>

                <div class="form-group">
                    <label for="paymentMethod">Payment Method</label>
                    <select id="paymentMethod" name="paymentMethod" class="form-control" required>
                        <option value="">Select payment method</option>
                        <option value="Cash">Cash</option>
                        <option value="Card">Card</option>
                        <option value="Online Banking">Online Banking</option>
                        <option value="Insurance">Insurance</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="notes">Notes (Optional)</label>
                    <textarea id="notes" name="notes" class="form-control" rows="3" placeholder="Additional notes..."></textarea>
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
    $('.collect-btn').on('click', function () {
        $('#modal-patient').val($(this).data('patient'));
        $('#modal-doctor').val($(this).data('doctor'));
        $('#modal-services').val($(this).data('services'));
        $('#modal-amount').val($(this).data('amount'));

        $('#modal-patient-view').text($(this).data('patient'));
        $('#modal-doctor-view').text($(this).data('doctor'));
        $('#modal-services-view').text($(this).data('services'));
        $('#modal-amount-view').text($(this).data('amount'));
    });
</script>
</body>
</html>
