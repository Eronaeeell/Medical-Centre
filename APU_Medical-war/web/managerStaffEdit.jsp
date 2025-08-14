<form id="editStaffForm" action="${pageContext.request.contextPath}/EditStaffServlet" method="post">
  <div class="modal-body">
    <input type="hidden" id="editId" name="id">
    <input type="hidden" id="editSource" name="source">
    <!-- submitted role value -->
    <input type="hidden" id="editRole" name="role">

    <div class="form-group">
      <label for="editName">Name</label>
      <input type="text" class="form-control" id="editName" name="name" required>
    </div>

    <div class="form-group">
      <label for="editEmail">Email</label>
      <input type="email" class="form-control" id="editEmail" name="email" required>
    </div>

    <div class="form-group">
      <label for="editPhone">Phone</label>
      <input type="text" class="form-control" id="editPhone" name="phone" required>
    </div>

    <!-- CHANGED: Role select (disabled for safety) -->
    <div class="form-group">
      <label for="editRoleSelect">Role</label>
      <select class="form-control" id="editRoleSelect" disabled>
        <option value="COUNTERSTAFF">Counter Staff</option>
        <option value="DOCTOR">Doctor</option>
      </select>
    </div>

    <div class="form-group">
      <label for="editDepartment">Department</label>
      <input type="text" class="form-control" id="editDepartment" name="department" required>
    </div>

    <div class="form-group">
      <label for="editStatus">Status</label>
      <select class="form-control" id="editStatus" name="status" required>
        <option value="Active">Active</option>
        <option value="Inactive">Inactive</option>
      </select>
    </div>

    <div class="form-group">
      <label for="editJoinDate">Join Date</label>
      <input type="date" class="form-control" id="editJoinDate" name="joinDate" required>
    </div>
  </div>

  <div class="modal-footer">
    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
    <button type="submit" class="btn btn-dark">Save Changes</button>
  </div>
</form>
