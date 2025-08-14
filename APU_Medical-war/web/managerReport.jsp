<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%@ include file="sidebar_manager.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manager - Reports & Analytics</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        body { background-color: #f9fafb; }
        .table td, .table th { vertical-align: middle; font-size: 0.9rem; }
        .badge-pill { border-radius: 9999px; padding: 0.35em 0.65em; font-size: 0.75rem; }
        .recent-report-row { background-color: #f6f8fa; border-radius: 0.5rem; margin-bottom: 0.75rem; }
        .recent-report-row:hover { background-color: #e7eef7; cursor:pointer; }
        .form-select, .form-control[type="date"] {
            border-radius: 0.45rem;
            border: 1.5px solid #d6e2f6;
            box-shadow: 0 2px 4px rgba(23,82,221,0.04);
            background-color: #f7fbff;
            transition: border-color 0.2s, box-shadow 0.2s;
            font-size: 1rem;
            font-weight: 500;
            color: #23395d;
        }
        .form-select:focus, .form-control[type="date"]:focus {
            border-color: #0d6efd;
            box-shadow: 0 0 0 0.12rem rgba(13,110,253,.13);
            background-color: #fff;
        }
        label.form-label {
            font-weight: 600;
            color: #1d3557;
            margin-bottom: 0.4rem;
            letter-spacing: 0.01em;
        }
        .badge-report-status {
            background: #198754;
            color: #fff !important;
            font-weight: 600;
            padding: 0.32em 0.7em;
            border-radius: 2em;
            font-size: 0.85em;
            letter-spacing: 0.02em;
        }
        .metrics-label { color: #7280a7; }
        .date-row { display: flex; gap: 1rem; }
        .date-row .form-group { flex: 1; }
        .modal-bg {
            position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
            background: rgba(60, 60, 60, 0.25); z-index: 2000; display: none;
            justify-content: center; align-items: center;
        }
        .modal-content-popup {
            background: #fff; border-radius: 1rem; box-shadow: 0 6px 32px rgba(0,0,0,0.2);
            padding: 2rem; max-width: 420px; width: 95vw; position: relative;
            animation: fadeIn 0.2s;
        }
        @keyframes fadeIn { from { opacity: 0; transform: scale(0.97);} to { opacity: 1; transform: scale(1);}}
        .close-btn { position: absolute; top: 10px; right: 16px; font-size: 1.6rem; color: #888; cursor: pointer; }
        .quick-date-buttons { margin-bottom: 1rem; }
        .quick-date-buttons .btn { margin-right: 0.5rem; margin-bottom: 0.5rem; }
        .error-message { color: #dc3545; font-size: 0.875rem; margin-top: 0.25rem; }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-left: 260px; padding: 20px; max-width: 1000px;">
    <div class="mb-3">
        <h4 class="mb-0 text-primary">
            <i class="bi bi-bar-chart-line mr-2"></i> Reports & Analytics
        </h4>
        <p class="text-muted">Generate comprehensive reports and analyze medical center performance</p>
    </div>

    <!-- Report Generation -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-white d-flex align-items-center">
            <i class="bi bi-file-earmark-text-fill text-primary mr-2"></i>
            <span class="h6 mb-0 ml-2">Generate New Report</span>
        </div>
        <div class="card-body">
            <form method="post" action="ManagerReportServlet" autocomplete="off" onsubmit="return validateForm()">
                <div class="form-group mb-3">
                    <label for="type" class="form-label">Report Type *</label>
                    <select id="type" name="type" class="form-select" required>
                        <option value="" disabled selected>Select report type</option>
                        <option value="financial">Financial Report</option>
                        <option value="staff">Staff Performance</option>
                        <option value="appointments">Appointment Analytics</option>
                    </select>
                </div>
                
                <!-- Quick Date Selection Buttons -->
                <div class="form-group mb-3">
                    <label class="form-label">Quick Date Selection</label>
                    <div class="quick-date-buttons">
                        <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setDateRange('today')">Today</button>
                        <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setDateRange('week')">This Week</button>
                        <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setDateRange('month')">This Month</button>
                        <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setDateRange('quarter')">This Quarter</button>
                        <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setDateRange('year')">This Year</button>
                    </div>
                </div>
                
                <div class="form-group mb-3">
                    <label class="form-label">Select Date Range *</label>
                    <div class="date-row">
                        <div class="form-group mb-0">
                            <label for="dateFrom" class="form-label" style="font-size: 0.875rem;">From Date</label>
                            <input type="date" class="form-control" id="dateFrom" name="dateFrom" required>
                        </div>
                        <div class="form-group mb-0">
                            <label for="dateTo" class="form-label" style="font-size: 0.875rem;">To Date</label>
                            <input type="date" class="form-control" id="dateTo" name="dateTo" required>
                        </div>
                    </div>
                    <div id="dateError" class="error-message" style="display: none;"></div>
                </div>
                
                <button type="submit" class="btn btn-primary w-100 mt-2">
                    <i class="bi bi-file-earmark-text mr-2"></i>
                    Generate Report
                </button>
            </form>
        </div>
    </div>

    <!-- Recent Reports -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-white">
            <span class="h6 mb-0">Recent Reports</span>
            <p class="text-muted mb-0" style="font-size: 0.9rem;">Click to view report details</p>
        </div>
        <div class="card-body">
            <%
                List<Map<String, Object>> recentReports = (List<Map<String, Object>>) request.getAttribute("recentReports");
            %>
            <div>
            <%
                if (recentReports != null && !recentReports.isEmpty()) {
                    for (Map<String, Object> report : recentReports) {
            %>
                <div class="d-flex justify-content-between align-items-center p-3 recent-report-row"
    onclick="showReportModal(
        '<%=report.get("name")%>',
        '<%=report.get("date")%>',
        '<%=report.get("type")%>',
        '<%=report.get("status")%>',
        '<%=report.get("size")%>',
        '<%=report.get("summary1") != null ? report.get("summary1").toString().replace("'", "\\'") : "" %>',
        '<%=report.get("summary2") != null ? report.get("summary2").toString().replace("'", "\\'") : "" %>',
        '<%=report.get("summary3") != null ? report.get("summary3").toString().replace("'", "\\'") : "" %>',
        '<%=report.get("summary4") != null ? report.get("summary4").toString().replace("'", "\\'") : "" %>',
        '<%=report.get("summary5") != null ? report.get("summary5").toString().replace("'", "\\'") : "" %>'
    )">
                    <div class="d-flex align-items-center">
                        <div class="d-flex align-items-center justify-content-center bg-primary bg-opacity-10 rounded-circle" style="width: 40px; height: 40px;">
                            <i class="bi bi-file-earmark-text text-primary" style="font-size: 1.4rem;"></i>
                        </div>
                        <div class="ml-3">
                            <div class="fw-semibold"><%= report.get("name") %></div>
                            <div class="text-muted" style="font-size: 0.92em;">
                                Generated on <%= report.get("date") %>
                                &nbsp;•&nbsp; <%= report.get("type") %>
                                &nbsp;•&nbsp; <%= report.get("size") %>
                                <% if (report.get("dateFrom") != null && report.get("dateTo") != null) { %>
                                    <br><span style="color: #007bff; font-weight: 500;">
                                        Date Range: <%= report.get("dateFrom") %> to <%= report.get("dateTo") %>
                                    </span>
                                <% } %>
                            </div>
                        </div>
                    </div>
                    <div class="d-flex align-items-center gap-2">
                        <span class="badge badge-report-status me-2"><%= report.get("status") %></span>
                    </div>
                </div>
            <%
                    }
                } else {
            %>
                <div class="text-muted text-center py-3">No reports generated yet.</div>
            <%
                }
            %>
            </div>
        </div>
    </div>
</div>

<!-- Success Modal (on report generation) -->
<% if(request.getAttribute("success") != null && recentReports != null && !recentReports.isEmpty()) {
       Map<String, Object> lastReport = recentReports.get(0); %>
<div class="modal-bg" id="successModal" style="display:flex;">
    <div class="modal-content-popup">
        <span class="close-btn" onclick="document.getElementById('successModal').style.display='none'">&times;</span>
        <div class="mb-3 text-success"><i class="bi bi-check-circle-fill" style="font-size:2.2rem;"></i></div>
        <h5 class="fw-bold mb-2">Report Generated!</h5>
        <div class="mb-2"><span class="fw-semibold">Name:</span> <%= lastReport.get("name") %></div>
        <div class="mb-2"><span class="fw-semibold">Date:</span> <%= lastReport.get("date") %></div>
        <div class="mb-2"><span class="fw-semibold">Type:</span> <%= lastReport.get("type") %></div>
        <div class="mb-2"><span class="fw-semibold">Status:</span> <%= lastReport.get("status") %></div>
        <div class="mb-2"><span class="fw-semibold">Size:</span> <%= lastReport.get("size") %></div>
        <% if (lastReport.get("dateFrom") != null && lastReport.get("dateTo") != null) { %>
        <div class="mb-2"><span class="fw-semibold">Date Range:</span> <%= lastReport.get("dateFrom") %> to <%= lastReport.get("dateTo") %></div>
        <% } %>
        <button class="btn btn-secondary w-100 mt-3" onclick="document.getElementById('successModal').style.display='none'">Close</button>
    </div>
</div>
<% } %>

<!-- Report Details Modal -->
<div class="modal-bg" id="reportModal">
    <div class="modal-content-popup" id="reportModalContent">
        <span class="close-btn" onclick="document.getElementById('reportModal').style.display='none'">&times;</span>
        <h5 class="fw-bold mb-2" id="modalReportName"></h5>
        <div class="mb-2"><span class="fw-semibold">Date:</span> <span id="modalReportDate"></span></div>
        <div class="mb-2"><span class="fw-semibold">Type:</span> <span id="modalReportType"></span></div>
        <div class="mb-2"><span class="fw-semibold">Status:</span> <span id="modalReportStatus"></span></div>
        <div class="mb-2"><span class="fw-semibold">Size:</span> <span id="modalReportSize"></span></div>
        <hr>
        <div id="modalReportSummary" style="white-space: pre-line; font-size: 1rem; color: #23395d;"></div>
        <button class="btn btn-secondary w-100 mt-3" onclick="document.getElementById('reportModal').style.display='none'">Close</button>
    </div>
</div>

<script>
// Set default date range on page load
document.addEventListener('DOMContentLoaded', function() {
    // Set default to current month
    setDateRange('month');
});

// Function to set date ranges
function setDateRange(period) {
    const today = new Date();
    let fromDate, toDate;
    
    switch(period) {
        case 'today':
            fromDate = toDate = today;
            break;
        case 'week':
            fromDate = new Date(today);
            fromDate.setDate(today.getDate() - today.getDay()); // Start of week (Sunday)
            toDate = new Date(fromDate);
            toDate.setDate(fromDate.getDate() + 6); // End of week (Saturday)
            break;
        case 'month':
            fromDate = new Date(today.getFullYear(), today.getMonth(), 1); // First day of month
            toDate = new Date(today.getFullYear(), today.getMonth() + 1, 0); // Last day of month
            break;
        case 'quarter':
            const quarter = Math.floor(today.getMonth() / 3);
            fromDate = new Date(today.getFullYear(), quarter * 3, 1);
            toDate = new Date(today.getFullYear(), quarter * 3 + 3, 0);
            break;
        case 'year':
            fromDate = new Date(today.getFullYear(), 0, 1); // January 1st
            toDate = new Date(today.getFullYear(), 11, 31); // December 31st
            break;
        default:
            fromDate = toDate = today;
    }
    
    document.getElementById('dateFrom').value = formatDate(fromDate);
    document.getElementById('dateTo').value = formatDate(toDate);
}

// Format date to YYYY-MM-DD
function formatDate(date) {
    return date.getFullYear() + '-' + 
           String(date.getMonth() + 1).padStart(2, '0') + '-' + 
           String(date.getDate()).padStart(2, '0');
}

// Validate form before submission
function validateForm() {
    const dateFrom = document.getElementById('dateFrom').value;
    const dateTo = document.getElementById('dateTo').value;
    const errorDiv = document.getElementById('dateError');
    
    if (!dateFrom || !dateTo) {
        errorDiv.textContent = 'Both start and end dates are required.';
        errorDiv.style.display = 'block';
        return false;
    }
    
    if (new Date(dateFrom) > new Date(dateTo)) {
        errorDiv.textContent = 'Start date must be before or equal to end date.';
        errorDiv.style.display = 'block';
        return false;
    }
    
    // Check if date range is too far in the future
    const today = new Date();
    if (new Date(dateFrom) > today) {
        errorDiv.textContent = 'Start date cannot be in the future.';
        errorDiv.style.display = 'block';
        return false;
    }
    
    errorDiv.style.display = 'none';
    return true;
}

// Show report modal with proper date range display
function showReportModal(name, date, type, status, size, summary1, summary2, summary3, summary4, summary5) {
    document.getElementById("modalReportName").innerText = name;
    document.getElementById("modalReportDate").innerText = date;
    document.getElementById("modalReportType").innerText = type;
    document.getElementById("modalReportStatus").innerText = status;
    document.getElementById("modalReportSize").innerText = size;

    let summary = "";
    const reportType = type.toLowerCase();
    if (reportType.includes("financial")) {
        summary =
            "Total Revenue: RM " + (summary1 || "0.00") + "\n" +
            "Completed Appointments: " + (summary2 || "0") + "\n" +
            "Pending Payments: " + (summary3 || "0") + "\n" +
            "Average Payment per Visit: RM " + (summary4 || "0.00");
    } else if (reportType.includes("staff")) {
        summary =
            "Active Doctors: " + (summary1 || "0") + "\n" +
            "Counter Staff: " + (summary2 || "0") + "\n" +
            "Top Doctor: " + (summary3 || "-") + "\n" +
            "Top Feedback Score: " + (summary4 || "-");
    } else if (reportType.includes("appointment")) {
        summary =
            "Total Appointments: " + (summary1 || "0") + "\n" +
            "Completed: " + (summary2 || "0") + ", Scheduled: " + (summary3 || "0") + "\n" +
            "Most Common Type: " + (summary4 || "-") + "\n" +
            "Feedback Summary: " + (summary5 || "-");
    } else {
        summary = "No summary available for this report type.";
    }

    document.getElementById("modalReportSummary").innerText = summary;
    document.getElementById("reportModal").style.display = 'flex';
}

// Hide modal when clicking outside
window.onclick = function(event) {
    if(event.target.className === "modal-bg") {
        event.target.style.display = "none";
    }
}
</script>
</body>
</html>