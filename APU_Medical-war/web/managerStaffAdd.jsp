<form id="addStaffForm"
      action="${pageContext.request.contextPath}/AddStaffServlet"
      method="post">

  <div class="modal-body">
    <div class="form-group">
      <label for="addName">Name</label>
      <input type="text" class="form-control" id="addName" name="name" required>
    </div>

    <div class="form-group">
      <label for="addEmail">Email</label>
      <input type="email" class="form-control" id="addEmail" name="email" required>
    </div>

    <!-- NEW -->
    <div class="form-group">
      <label for="addPassword">Password</label>
      <input type="password" class="form-control" id="addPassword" name="password" required>
    </div>

    <div class="form-group">
      <label for="addPhone">Phone</label>
      <input type="text" class="form-control" id="addPhone" name="phone" required>
    </div>

    <div class="form-group">
      <label for="addRole">Role</label>
      <select class="form-control" id="addRole" name="role" required>
        <option value="" disabled selected>Select role</option>
        <option value="COUNTERSTAFF">Counter Staff</option>
        <option value="DOCTOR">Doctor</option>
      </select>
    </div>

    <div class="form-group">
      <label for="addDepartment">Department</label>
      <input type="text" class="form-control" id="addDepartment" name="department" required>
    </div>

    <div class="form-group">
      <label for="addStatus">Status</label>
      <select class="form-control" id="addStatus" name="status" required>
        <option value="Active">Active</option>
        <option value="Inactive">Inactive</option>
      </select>
    </div>

    <div class="form-group">
      <label for="addJoinDate">Join Date</label>
      <input type="date" class="form-control" id="addJoinDate" name="joinDate" required>
    </div>
  </div>

  <div class="modal-footer">
    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
    <button type="submit" class="btn btn-dark">Add Staff</button>
  </div>
</form>
