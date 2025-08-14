<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ include file="sidebar_manager.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manager - Staff Management</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body { background-color: #f9fafb; }
        .table thead { background-color: #f0f2f5; }
        .table td, .table th { vertical-align: middle; font-size: 0.9rem; }
        .badge-pill { border-radius: 9999px; padding: 0.35em 0.65em; font-size: 0.75rem; }
        .action-btn { background: none; border: none; cursor: pointer; padding: 0.2rem; }
        .action-btn i { font-size: 1rem; color: #333; }
        .action-btn.edit i { color: #0d6efd; }
        .action-btn.delete i { color: #dc3545; }
        .action-btn:hover i { opacity: 0.8; }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-left: 260px; padding: 20px; max-width: 1100px;">

    <!-- Title + Add Staff -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0"><i class="fas fa-users-cog mr-2"></i> Staff Management</h4>
        <button class="btn btn-dark" data-toggle="modal" data-target="#addStaffModal">
            <i class="fas fa-user-plus mr-1"></i> Add Staff
        </button>
    </div>
    <p class="text-muted mb-3">Manage staff from <strong>Counter Staff</strong> and <strong>Doctor</strong> tables.</p>

    <!-- Summary Cards (combined) -->
    <div class="row mb-4">
        <div class="col-md-3 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-users fa-2x text-primary mb-2"></i>
                    <h6 class="text-muted">Total Staff</h6>
                    <h4><%= request.getAttribute("totalStaff") != null ? request.getAttribute("totalStaff") : "0" %></h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-user-check fa-2x text-success mb-2"></i>
                    <h6 class="text-muted">Active Staff</h6>
                    <h4><%= request.getAttribute("activeStaff") != null ? request.getAttribute("activeStaff") : "0" %></h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-user-slash fa-2x text-danger mb-2"></i>
                    <h6 class="text-muted">Inactive Staff</h6>
                    <h4><%= request.getAttribute("inactiveStaff") != null ? request.getAttribute("inactiveStaff") : "0" %></h4>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-2">
            <div class="card shadow-sm text-center">
                <div class="card-body">
                    <i class="fas fa-building fa-2x text-warning mb-2"></i>
                    <h6 class="text-muted">Departments</h6>
                    <h4><%= request.getAttribute("totalDepartments") != null ? request.getAttribute("totalDepartments") : "0" %></h4>
                </div>
            </div>
        </div>
    </div>

    <!-- COUNTER STAFF TABLE -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-white">
            <h5 class="mb-0"><i class="fas fa-user-tie mr-2"></i> Counter Staff</h5>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-striped table-hover mb-0">
                    <thead>
                    <tr>
                        <th>ID</th><th>Name</th><th>Email</th><th>Phone</th>
                        <th>Role</th><th>Department</th><th>Status</th><th>Join Date</th><th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        List<Map<String, String>> counterStaffList =
                            (List<Map<String, String>>) request.getAttribute("counterStaffList");
                        if (counterStaffList != null && !counterStaffList.isEmpty()) {
                            for (Map<String, String> s : counterStaffList) {
                    %>
                    <tr>
                        <td><%= s.get("id") %></td>
                        <td><%= s.get("name") %></td>
                        <td><%= s.get("email") %></td>
                        <td><%= s.get("phone") %></td>
                        <td><%= s.get("role") %></td>
                        <td><%= s.get("department") %></td>
                        <td>
                            <% if ("Active".equalsIgnoreCase(s.get("status"))) { %>
                                <span class="badge badge-success badge-pill">Active</span>
                            <% } else { %>
                                <span class="badge badge-secondary badge-pill">Inactive</span>
                            <% } %>
                        </td>
                        <td><%= s.get("joinDate") %></td>
                        <td>
                            <button class="action-btn edit"
                                    data-toggle="modal" data-target="#editStaffModal"
                                    data-id="<%= s.get("id") %>" data-name="<%= s.get("name") %>"
                                    data-email="<%= s.get("email") %>" data-phone="<%= s.get("phone") %>"
                                    data-role="<%= s.get("role") %>" data-department="<%= s.get("department") %>"
                                    data-status="<%= s.get("status") %>" data-joindate="<%= s.get("joinDate") %>"
                                    data-source="COUNTERSTAFF">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="action-btn delete delete-btn"
                                    data-id="<%= s.get("id") %>" data-source="COUNTERSTAFF">
                                <i class="fas fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                    <%      }
                        } else { %>
                    <tr><td colspan="9" class="text-center text-muted">No counter staff found.</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- DOCTOR TABLE -->
    <div class="card shadow-sm">
        <div class="card-header bg-white">
            <h5 class="mb-0"><i class="fas fa-user-md mr-2"></i> Doctor</h5>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-striped table-hover mb-0">
                    <thead>
                    <tr>
                        <th>ID</th><th>Name</th><th>Email</th><th>Phone</th>
                        <th>Role</th><th>Department</th><th>Status</th><th>Join Date</th><th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        List<Map<String, String>> doctorList =
                            (List<Map<String, String>>) request.getAttribute("doctorList");
                        if (doctorList != null && !doctorList.isEmpty()) {
                            for (Map<String, String> s : doctorList) {
                    %>
                    <tr>
                        <td><%= s.get("id") %></td>
                        <td><%= s.get("name") %></td>
                        <td><%= s.get("email") %></td>
                        <td><%= s.get("phone") %></td>
                        <td><%= s.get("role") %></td>
                        <td><%= s.get("department") %></td>
                        <td>
                            <% if ("Active".equalsIgnoreCase(s.get("status"))) { %>
                                <span class="badge badge-success badge-pill">Active</span>
                            <% } else { %>
                                <span class="badge badge-secondary badge-pill">Inactive</span>
                            <% } %>
                        </td>
                        <td><%= s.get("joinDate") %></td>
                        <td>
                            <button class="action-btn edit"
                                    data-toggle="modal" data-target="#editStaffModal"
                                    data-id="<%= s.get("id") %>" data-name="<%= s.get("name") %>"
                                    data-email="<%= s.get("email") %>" data-phone="<%= s.get("phone") %>"
                                    data-role="<%= s.get("role") %>" data-department="<%= s.get("department") %>"
                                    data-status="<%= s.get("status") %>" data-joindate="<%= s.get("joinDate") %>"
                                    data-source="DOCTOR">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="action-btn delete delete-btn"
                                    data-id="<%= s.get("id") %>" data-source="DOCTOR">
                                <i class="fas fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                    <%      }
                        } else { %>
                    <tr><td colspan="9" class="text-center text-muted">No doctors found.</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>

<!-- Add Staff Modal (unchanged) -->
<div class="modal fade" id="addStaffModal">
    <div class="modal-dialog">
        <form method="post" action="AddStaffServlet" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add Staff</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <%@ include file="managerStaffAdd.jsp" %>
        </form>
    </div>
</div>

<!-- Edit Staff Modal (unchanged; add a hidden #editSource input if you want) -->
<div class="modal fade" id="editStaffModal">
    <div class="modal-dialog">
        <form method="post" action="EditStaffServlet" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Edit Staff</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <%@ include file="managerStaffEdit.jsp" %>
        </form>
    </div>
</div>

<script>
$(function() {
    // ADD
    $('#addStaffModal form').submit(function(e) {
        e.preventDefault();
        $.post('AddStaffServlet', $(this).serialize())
         .done(function(){ $('#addStaffModal').modal('hide'); alert('Staff added successfully!'); location.reload(); })
         .fail(function(xhr){ alert('Error adding staff: ' + xhr.responseText); });
    });

    // EDIT
    $('#editStaffModal form').submit(function(e) {
        e.preventDefault();
        $.post('EditStaffServlet', $(this).serialize())
         .done(function(){ $('#editStaffModal').modal('hide'); alert('Staff updated successfully!'); location.reload(); })
         .fail(function(xhr){ alert('Error updating staff: ' + xhr.responseText); });
    });

    // DELETE
    $(document).on('click', '.delete-btn', function() {
        if (!confirm('Are you sure you want to delete this staff?')) return;
        var id = $(this).data('id');
        var source = $(this).data('source');
        $.post('DeleteStaffServlet', { id: id, source: source })
         .done(function(){ alert('Staff deleted successfully!'); location.reload(); })
         .fail(function(xhr){ alert('Error deleting staff: ' + xhr.responseText); });
    });

    // Fill Edit Modal
    $('#editStaffModal').on('show.bs.modal', function (event) {
        var b = $(event.relatedTarget);
        $('#editId').val(b.data('id'));
        $('#editName').val(b.data('name'));
        $('#editEmail').val(b.data('email'));
        $('#editPhone').val(b.data('phone'));
        $('#editRole').val(b.data('role'));
        $('#editDepartment').val(b.data('department'));
        $('#editStatus').val(b.data('status'));
        $('#editJoinDate').val(b.data('joindate'));
        if ($('#editSource').length) $('#editSource').val(b.data('source')); // optional hidden input
    });
});
</script>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>
